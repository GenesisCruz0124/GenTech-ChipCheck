package com.gentech.chipcheck.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    /** Frozen JSON snapshot of the DecodedChip at scan time -- see ScanHistoryRepository. */
    val decodedSnapshotJson: String,
    val vendor: String?,
    val normalizedPartNumber: String,
    val hasUnknownSegments: Boolean,
    val source: String,
    val timestamp: Long
)
