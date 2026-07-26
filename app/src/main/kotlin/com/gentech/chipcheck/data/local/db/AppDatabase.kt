package com.gentech.chipcheck.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ScanHistoryEntity::class, UnmappedCodeEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanHistoryDao(): ScanHistoryDao
    abstract fun unmappedCodeDao(): UnmappedCodeDao
}
