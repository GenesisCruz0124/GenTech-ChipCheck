package com.gentech.chipcheck.data.firmware

import com.gentech.chipcheck.domain.repository.FirmwareVersionEntry
import kotlinx.serialization.json.Json

object FirmwareVersionJsonParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(rawJson: String): List<FirmwareVersionEntry> =
        json.decodeFromString<List<FirmwareVersionDto>>(rawJson).map {
            FirmwareVersionEntry(model = it.model, buildVersion = it.buildVersion)
        }
}
