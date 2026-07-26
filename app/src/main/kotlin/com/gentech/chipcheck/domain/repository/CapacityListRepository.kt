package com.gentech.chipcheck.domain.repository

/**
 * A category as printed on the shop's module reference sheet (e.g. "EMMC 16GB (A+)"), holding
 * every module code catalogued under it. [note] carries qualifiers like "Main Xanh -- Huawei/Honor".
 */
data class CapacityCategory(
    val label: String,
    val note: String?,
    val codes: List<String>
)

interface CapacityListRepository {
    suspend fun getCategories(): List<CapacityCategory>
}
