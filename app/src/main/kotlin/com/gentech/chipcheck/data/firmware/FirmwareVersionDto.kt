package com.gentech.chipcheck.data.firmware

import kotlinx.serialization.Serializable

@Serializable
data class FirmwareVersionDto(
    val model: String,
    val buildVersion: String
)
