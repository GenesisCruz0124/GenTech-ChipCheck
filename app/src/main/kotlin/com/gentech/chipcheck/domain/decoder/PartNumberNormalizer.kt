package com.gentech.chipcheck.domain.decoder

object PartNumberNormalizer {
    private val stripPattern = Regex("[\\s-]")

    fun normalize(rawInput: String): String =
        rawInput.uppercase().replace(stripPattern, "")
}
