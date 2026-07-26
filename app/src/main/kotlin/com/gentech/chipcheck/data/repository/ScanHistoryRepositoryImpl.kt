package com.gentech.chipcheck.data.repository

import com.gentech.chipcheck.data.local.db.ScanHistoryDao
import com.gentech.chipcheck.data.local.db.ScanHistoryEntity
import com.gentech.chipcheck.domain.decoder.DecodedChip
import com.gentech.chipcheck.domain.repository.ScanHistoryItem
import com.gentech.chipcheck.domain.repository.ScanHistoryRepository
import com.gentech.chipcheck.domain.repository.ScanSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Singleton
class ScanHistoryRepositoryImpl @Inject constructor(
    private val dao: ScanHistoryDao
) : ScanHistoryRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override fun observeAll(): Flow<List<ScanHistoryItem>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override fun search(query: String): Flow<List<ScanHistoryItem>> =
        dao.search(query).map { entities -> entities.map { it.toDomain() } }

    override suspend fun save(decodedChip: DecodedChip, source: ScanSource): Long =
        dao.insert(decodedChip.toEntity(id = 0, source = source, timestamp = System.currentTimeMillis()))

    override suspend fun getById(id: Long): ScanHistoryItem? = dao.getById(id)?.toDomain()

    override suspend fun delete(id: Long) = dao.deleteById(id)

    override suspend fun restore(item: ScanHistoryItem) {
        dao.insert(item.decodedChip.toEntity(id = item.id, source = item.source, timestamp = item.timestamp))
    }

    override suspend fun clearAll() = dao.clearAll()

    private fun DecodedChip.toEntity(id: Long, source: ScanSource, timestamp: Long) = ScanHistoryEntity(
        id = id,
        decodedSnapshotJson = json.encodeToString(this),
        vendor = vendor,
        normalizedPartNumber = normalizedPartNumber,
        hasUnknownSegments = hasUnknownSegments,
        source = source.name,
        timestamp = timestamp
    )

    private fun ScanHistoryEntity.toDomain(): ScanHistoryItem = ScanHistoryItem(
        id = id,
        decodedChip = json.decodeFromString<DecodedChip>(decodedSnapshotJson),
        source = ScanSource.valueOf(source),
        timestamp = timestamp
    )
}
