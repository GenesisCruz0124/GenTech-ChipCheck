package com.gentech.chipcheck.data.rules

import com.gentech.chipcheck.domain.decoder.ChipRule
import com.gentech.chipcheck.domain.decoder.SegmentDefinition
import com.gentech.chipcheck.domain.decoder.SegmentStrategy
import kotlinx.serialization.json.Json

/**
 * Pure String -> domain-model parsing, no Android dependency, so it's directly unit-testable
 * against the real shipping chip_rules.json.
 */
object ChipRuleJsonParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(rawJson: String): List<ChipRule> {
        val dtos = json.decodeFromString<List<ChipRuleDto>>(rawJson)
        return dtos.map { it.toDomain() }
    }

    private fun ChipRuleDto.toDomain(): ChipRule = ChipRule(
        vendor = vendor,
        matchPrefixes = matchPrefixes,
        example = example,
        segments = segments.map { it.toDomain() }
    )

    private fun SegmentDefinitionDto.toDomain(): SegmentDefinition = SegmentDefinition(
        name = name,
        label = label,
        strategy = when (strategy) {
            "prefix" -> SegmentStrategy.Prefix
            "fixedLength" -> SegmentStrategy.FixedLength(
                length ?: throw IllegalArgumentException("Segment '$name' uses fixedLength strategy but has no length")
            )
            else -> throw IllegalArgumentException("Unknown segment strategy '$strategy' for segment '$name'")
        },
        map = map
    )
}
