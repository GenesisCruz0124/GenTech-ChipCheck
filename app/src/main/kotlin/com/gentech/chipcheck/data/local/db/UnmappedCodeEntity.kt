package com.gentech.chipcheck.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unmapped_codes")
data class UnmappedCodeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val vendorGuess: String?,
    val segmentName: String?,
    val rawCode: String,
    val fullPartNumber: String,
    val timestamp: Long,
    val reviewed: Boolean
)
