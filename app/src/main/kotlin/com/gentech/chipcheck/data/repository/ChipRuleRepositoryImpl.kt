package com.gentech.chipcheck.data.repository

import com.gentech.chipcheck.data.rules.ChipRuleAssetLoader
import com.gentech.chipcheck.domain.decoder.ChipRule
import com.gentech.chipcheck.domain.repository.ChipRuleRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@Singleton
class ChipRuleRepositoryImpl @Inject constructor(
    private val assetLoader: ChipRuleAssetLoader
) : ChipRuleRepository {

    private val mutex = Mutex()
    private var cachedRules: List<ChipRule>? = null

    override suspend fun getRules(): List<ChipRule> {
        cachedRules?.let { return it }
        return mutex.withLock {
            cachedRules ?: withContext(Dispatchers.IO) { assetLoader.load() }.also { cachedRules = it }
        }
    }
}
