package com.gentech.chipcheck.domain.decoder

data class SegmentDefinition(
    val name: String,
    val label: String,
    val strategy: SegmentStrategy,
    val map: Map<String, String>
)
