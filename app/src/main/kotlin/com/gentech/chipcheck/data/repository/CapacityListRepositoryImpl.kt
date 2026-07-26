package com.gentech.chipcheck.data.repository

import com.gentech.chipcheck.data.capacitylist.CapacityListAssetLoader
import com.gentech.chipcheck.domain.repository.CapacityCategory
import com.gentech.chipcheck.domain.repository.CapacityListRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@Singleton
class CapacityListRepositoryImpl @Inject constructor(
    private val assetLoader: CapacityListAssetLoader
) : CapacityListRepository {

    private val mutex = Mutex()
    private var cachedCategories: List<CapacityCategory>? = null

    override suspend fun getCategories(): List<CapacityCategory> {
        cachedCategories?.let { return it }
        return mutex.withLock {
            cachedCategories ?: withContext(Dispatchers.IO) { assetLoader.load() }.also { cachedCategories = it }
        }
    }
}
