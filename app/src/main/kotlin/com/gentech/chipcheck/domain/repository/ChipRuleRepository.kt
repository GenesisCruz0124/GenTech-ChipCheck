package com.gentech.chipcheck.domain.repository

import com.gentech.chipcheck.domain.decoder.ChipRule

interface ChipRuleRepository {
    suspend fun getRules(): List<ChipRule>
}
