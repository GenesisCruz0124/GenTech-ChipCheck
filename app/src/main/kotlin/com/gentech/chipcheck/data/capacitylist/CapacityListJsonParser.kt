package com.gentech.chipcheck.data.capacitylist

import com.gentech.chipcheck.domain.repository.CapacityCategory
import kotlinx.serialization.json.Json

object CapacityListJsonParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(rawJson: String): List<CapacityCategory> =
        json.decodeFromString<List<CapacityCategoryDto>>(rawJson).map {
            CapacityCategory(label = it.label, note = it.note, codes = it.codes)
        }
}
