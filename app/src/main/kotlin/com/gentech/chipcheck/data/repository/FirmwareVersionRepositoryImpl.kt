package com.gentech.chipcheck.data.repository

import com.gentech.chipcheck.data.firmware.FirmwareVersionAssetLoader
import com.gentech.chipcheck.domain.repository.FirmwareVersionEntry
import com.gentech.chipcheck.domain.repository.FirmwareVersionRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@Singleton
class FirmwareVersionRepositoryImpl @Inject constructor(
    private val assetLoader: FirmwareVersionAssetLoader
) : FirmwareVersionRepository {

    private val mutex = Mutex()
    private var cachedEntries: List<FirmwareVersionEntry>? = null

    override suspend fun getEntries(): List<FirmwareVersionEntry> {
        cachedEntries?.let { return it }
        return mutex.withLock {
            cachedEntries ?: withContext(Dispatchers.IO) { assetLoader.load() }.also { cachedEntries = it }
        }
    }
}
