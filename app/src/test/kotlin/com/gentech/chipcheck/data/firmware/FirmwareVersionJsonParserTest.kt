package com.gentech.chipcheck.data.firmware

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FirmwareVersionJsonParserTest {

    private fun loadRealAsset(): String =
        requireNotNull(
            Thread.currentThread().contextClassLoader?.getResourceAsStream("firmware_versions.json")
        ).bufferedReader().use { it.readText() }

    @Test
    fun `parses every entry from the real shipping asset`() {
        val entries = FirmwareVersionJsonParser.parse(loadRealAsset())

        assertTrue(entries.isNotEmpty())
        assertTrue(entries.any { it.model == "SAMSUNG GALAXY A05" && it.buildVersion == "A055FXXSHDZF1" })
    }

    @Test
    fun `parses a minimal inline example`() {
        val json = """[{"model":"TEST MODEL","buildVersion":"BUILD123"}]"""

        val entries = FirmwareVersionJsonParser.parse(json)

        assertEquals(1, entries.size)
        assertEquals("TEST MODEL", entries.first().model)
        assertEquals("BUILD123", entries.first().buildVersion)
    }
}
