package com.gentech.chipcheck.domain.decoder

import javax.inject.Inject

/**
 * Data-driven decoder: matches a vendor by its longest matching prefix, then walks that
 * vendor's declared segments left-to-right consuming characters from the normalized input.
 * Never invents a value -- an unmapped code always comes back as [Confidence.UNKNOWN] with
 * the raw characters preserved, whether that's a whole unrecognized prefix, one unmapped
 * segment code, or leftover characters past the last declared segment.
 */
class ChipDecoder @Inject constructor() {

    fun decode(rawInput: String, rules: List<ChipRule>): DecodedChip {
        val normalized = PartNumberNormalizer.normalize(rawInput)
        val dateCodeHint = DateCodeParser.parse(normalized)

        val (rule, matchedPrefix) = selectVendorRule(normalized, rules)
            ?: return DecodedChip(
                rawInput = rawInput,
                normalizedPartNumber = normalized,
                vendor = null,
                segments = listOf(
                    DecodedSegment(
                        name = "unrecognized",
                        label = "Part Number",
                        rawValue = normalized,
                        decodedValue = null,
                        confidence = Confidence.UNKNOWN
                    )
                ),
                dateCodeHint = dateCodeHint
            )

        var cursor = matchedPrefix.length
        val segments = mutableListOf<DecodedSegment>()

        for (segmentDef in rule.segments) {
            when (val strategy = segmentDef.strategy) {
                is SegmentStrategy.Prefix -> {
                    val decoded = segmentDef.map[matchedPrefix]
                    segments += DecodedSegment(
                        name = segmentDef.name,
                        label = segmentDef.label,
                        rawValue = matchedPrefix,
                        decodedValue = decoded,
                        confidence = decoded.toConfidence()
                    )
                }

                is SegmentStrategy.FixedLength -> {
                    if (cursor >= normalized.length) {
                        segments += DecodedSegment(
                            name = segmentDef.name,
                            label = segmentDef.label,
                            rawValue = "",
                            decodedValue = null,
                            confidence = Confidence.UNKNOWN
                        )
                        continue
                    }
                    val end = (cursor + strategy.length).coerceAtMost(normalized.length)
                    val raw = normalized.substring(cursor, end)
                    cursor = end
                    val decoded = segmentDef.map[raw]
                    segments += DecodedSegment(
                        name = segmentDef.name,
                        label = segmentDef.label,
                        rawValue = raw,
                        decodedValue = decoded,
                        confidence = decoded.toConfidence()
                    )
                }
            }
        }

        if (cursor < normalized.length) {
            segments += DecodedSegment(
                name = "remaining",
                label = "Additional code",
                rawValue = normalized.substring(cursor),
                decodedValue = null,
                confidence = Confidence.UNKNOWN
            )
        }

        return DecodedChip(
            rawInput = rawInput,
            normalizedPartNumber = normalized,
            vendor = rule.vendor,
            segments = segments,
            dateCodeHint = dateCodeHint
        )
    }

    private fun selectVendorRule(normalized: String, rules: List<ChipRule>): Pair<ChipRule, String>? =
        rules
            .mapNotNull { rule ->
                val bestPrefix = longestMatchingPrefix(normalized, rule.matchPrefixes) ?: return@mapNotNull null
                rule to bestPrefix
            }
            .maxByOrNull { (_, prefix) -> prefix.length }

    private fun longestMatchingPrefix(normalized: String, prefixes: List<String>): String? =
        prefixes.filter { normalized.startsWith(it) }.maxByOrNull { it.length }

    private fun String?.toConfidence(): Confidence =
        if (this != null) Confidence.KNOWN else Confidence.UNKNOWN
}
