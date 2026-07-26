package com.gentech.chipcheck.domain.repository

data class FirmwareVersionEntry(
    val model: String,
    val buildVersion: String
)

interface FirmwareVersionRepository {
    suspend fun getEntries(): List<FirmwareVersionEntry>
}
