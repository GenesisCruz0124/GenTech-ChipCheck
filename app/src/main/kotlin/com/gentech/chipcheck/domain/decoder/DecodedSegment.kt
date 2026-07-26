package com.gentech.chipcheck.domain.decoder

import kotlinx.serialization.Serializable

@Serializable
data class DecodedSegment(
    val name: String,
    val label: String,
    val rawValue: String,
    val decodedValue: String?,
    val confidence: Confidence
)
