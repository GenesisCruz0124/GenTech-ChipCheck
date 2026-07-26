package com.gentech.chipcheck.data.repository

import com.gentech.chipcheck.data.local.db.UnmappedCodeDao
import com.gentech.chipcheck.data.local.db.UnmappedCodeEntity
import com.gentech.chipcheck.domain.repository.UnmappedCodeItem
import com.gentech.chipcheck.domain.repository.UnmappedCodeRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class UnmappedCodeRepositoryImpl @Inject constructor(
    private val dao: UnmappedCodeDao
) : UnmappedCodeRepository {

    override fun observeAll(): Flow<List<UnmappedCodeItem>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun submit(vendorGuess: String?, segmentName: String?, rawCode: String, fullPartNumber: String) {
        dao.insert(
            UnmappedCodeEntity(
                vendorGuess = vendorGuess,
                segmentName = segmentName,
                rawCode = rawCode,
                fullPartNumber = fullPartNumber,
                timestamp = System.currentTimeMillis(),
                reviewed = false
            )
        )
    }

    override suspend fun markReviewed(id: Long, reviewed: Boolean) = dao.markReviewed(id, reviewed)

    override suspend fun delete(id: Long) = dao.deleteById(id)

    private fun UnmappedCodeEntity.toDomain() = UnmappedCodeItem(
        id = id,
        vendorGuess = vendorGuess,
        segmentName = segmentName,
        rawCode = rawCode,
        fullPartNumber = fullPartNumber,
        timestamp = timestamp,
        reviewed = reviewed
    )
}
