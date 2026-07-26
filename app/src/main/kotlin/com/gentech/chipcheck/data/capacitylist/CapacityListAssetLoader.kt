package com.gentech.chipcheck.data.capacitylist

import android.content.Context
import com.gentech.chipcheck.domain.repository.CapacityCategory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val CAPACITY_LIST_ASSET_PATH = "capacity_list.json"

class CapacityListAssetLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun load(): List<CapacityCategory> {
        val json = context.assets.open(CAPACITY_LIST_ASSET_PATH).bufferedReader().use { it.readText() }
        return CapacityListJsonParser.parse(json)
    }
}
