package com.gentech.chipcheck.domain.decoder

import kotlinx.serialization.Serializable

/**
 * A trailing 4-digit code is ambiguous between YYWW and WWYY layouts, so both readings
 * are surfaced side by side rather than picked for the user.
 */
@Serializable
data class DateCodeHint(
    val rawDigits: String,
    val yywwReading: String,
    val wwyyReading: String
)

object DateCodeParser {
    private val trailingFourDigits = Regex("(\\d{4})$")

    fun parse(normalizedPartNumber: String): DateCodeHint? {
        val digits = trailingFourDigits.find(normalizedPartNumber)?.groupValues?.get(1) ?: return null
        val first = digits.substring(0, 2)
        val second = digits.substring(2, 4)
        return DateCodeHint(
            rawDigits = digits,
            yywwReading = "20$first, Week $second",
            wwyyReading = "Week $first, 20$second"
        )
    }
}
