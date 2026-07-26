package com.gentech.chipcheck.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {
    @Insert
    suspend fun insert(entity: ScanHistoryEntity): Long

    @Query("SELECT * FROM scan_history WHERE id = :id")
    suspend fun getById(id: Long): ScanHistoryEntity?

    @Query("DELETE FROM scan_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<ScanHistoryEntity>>

    @Query(
        "SELECT * FROM scan_history " +
            "WHERE normalizedPartNumber LIKE '%' || :query || '%' OR vendor LIKE '%' || :query || '%' " +
            "ORDER BY timestamp DESC"
    )
    fun search(query: String): Flow<List<ScanHistoryEntity>>

    @Query("DELETE FROM scan_history")
    suspend fun clearAll()
}
