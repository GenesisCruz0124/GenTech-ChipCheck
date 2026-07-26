package com.gentech.chipcheck.domain.decoder

data class ChipRule(
    val vendor: String,
    val matchPrefixes: List<String>,
    val example: String,
    val segments: List<SegmentDefinition>
)
