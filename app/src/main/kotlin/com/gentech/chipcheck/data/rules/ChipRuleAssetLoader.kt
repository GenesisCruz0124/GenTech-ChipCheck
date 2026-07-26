package com.gentech.chipcheck.data.rules

import android.content.Context
import com.gentech.chipcheck.domain.decoder.ChipRule
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val RULES_ASSET_PATH = "chip_rules.json"

class ChipRuleAssetLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun load(): List<ChipRule> {
        val json = context.assets.open(RULES_ASSET_PATH).bufferedReader().use { it.readText() }
        return ChipRuleJsonParser.parse(json)
    }
}
