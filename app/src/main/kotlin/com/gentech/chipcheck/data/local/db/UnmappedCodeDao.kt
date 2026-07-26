package com.gentech.chipcheck.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UnmappedCodeDao {
    @Insert
    suspend fun insert(entity: UnmappedCodeEntity): Long

    @Query("SELECT * FROM unmapped_codes ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<UnmappedCodeEntity>>

    @Query("UPDATE unmapped_codes SET reviewed = :reviewed WHERE id = :id")
    suspend fun markReviewed(id: Long, reviewed: Boolean)

    @Query("DELETE FROM unmapped_codes WHERE id = :id")
    suspend fun deleteById(id: Long)
}
