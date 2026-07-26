package com.gentech.chipcheck.data.capacitylist

import kotlinx.serialization.Serializable

@Serializable
data class CapacityCategoryDto(
    val label: String,
    val note: String? = null,
    val codes: List<String>
)
