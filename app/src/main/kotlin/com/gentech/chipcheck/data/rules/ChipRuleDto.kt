package com.gentech.chipcheck.data.rules

import kotlinx.serialization.Serializable

@Serializable
data class ChipRuleDto(
    val vendor: String,
    val matchPrefixes: List<String>,
    val example: String,
    val segments: List<SegmentDefinitionDto>
)

@Serializable
data class SegmentDefinitionDto(
    val name: String,
    val label: String,
    val strategy: String,
    val length: Int? = null,
    val map: Map<String, String> = emptyMap()
)
