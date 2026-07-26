package com.gentech.chipcheck.domain.repository

import kotlinx.coroutines.flow.Flow

data class UnmappedCodeItem(
    val id: Long,
    val vendorGuess: String?,
    val segmentName: String?,
    val rawCode: String,
    val fullPartNumber: String,
    val timestamp: Long,
    val reviewed: Boolean
)

interface UnmappedCodeRepository {
    fun observeAll(): Flow<List<UnmappedCodeItem>>
    suspend fun submit(vendorGuess: String?, segmentName: String?, rawCode: String, fullPartNumber: String)
    suspend fun markReviewed(id: Long, reviewed: Boolean)
    suspend fun delete(id: Long)
}
