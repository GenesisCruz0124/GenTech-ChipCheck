package com.gentech.chipcheck.data.firmware

import android.content.Context
import com.gentech.chipcheck.domain.repository.FirmwareVersionEntry
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val FIRMWARE_VERSIONS_ASSET_PATH = "firmware_versions.json"

class FirmwareVersionAssetLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun load(): List<FirmwareVersionEntry> {
        val json = context.assets.open(FIRMWARE_VERSIONS_ASSET_PATH).bufferedReader().use { it.readText() }
        return FirmwareVersionJsonParser.parse(json)
    }
}
