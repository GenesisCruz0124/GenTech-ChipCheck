package com.gentech.chipcheck.domain.repository

import com.gentech.chipcheck.domain.decoder.DecodedChip
import kotlinx.coroutines.flow.Flow

enum class ScanSource { CAMERA_OCR, MANUAL }

/**
 * [decodedChip] is a frozen snapshot taken at scan time, so a later chip_rules.json
 * update never retroactively changes what an old history entry displays.
 */
data class ScanHistoryItem(
    val id: Long,
    val decodedChip: DecodedChip,
    val source: ScanSource,
    val timestamp: Long
)

interface ScanHistoryRepository {
    fun observeAll(): Flow<List<ScanHistoryItem>>
    fun search(query: String): Flow<List<ScanHistoryItem>>
    suspend fun save(decodedChip: DecodedChip, source: ScanSource): Long
    suspend fun getById(id: Long): ScanHistoryItem?
    suspend fun delete(id: Long)
    /** Re-inserts a previously deleted item (used by the History screen's undo snackbar). */
    suspend fun restore(item: ScanHistoryItem)
    suspend fun clearAll()
}
