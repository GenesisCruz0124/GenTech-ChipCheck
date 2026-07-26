package com.gentech.chipcheck.domain.decoder

import kotlinx.serialization.Serializable

@Serializable
data class DecodedChip(
    val rawInput: String,
    val normalizedPartNumber: String,
    val vendor: String?,
    val segments: List<DecodedSegment>,
    val dateCodeHint: DateCodeHint?
) {
    val hasUnknownSegments: Boolean
        get() = segments.any { it.confidence == Confidence.UNKNOWN }
}
