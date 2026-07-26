package com.gentech.chipcheck.data.capacitylist

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CapacityListJsonParserTest {

    private fun loadRealAsset(): String =
        requireNotNull(
            Thread.currentThread().contextClassLoader?.getResourceAsStream("capacity_list.json")
        ).bufferedReader().use { it.readText() }

    @Test
    fun `parses all ten categories from the real shipping asset`() {
        val categories = CapacityListJsonParser.parse(loadRealAsset())

        assertEquals(10, categories.size)
        assertTrue(categories.all { it.codes.isNotEmpty() })
    }

    @Test
    fun `Huawei-Honor categories carry their note`() {
        val categories = CapacityListJsonParser.parse(loadRealAsset())

        val huaweiCategory = categories.first { it.label.contains("Main Xanh") }
        assertTrue(huaweiCategory.note!!.contains("Huawei"))
    }

    @Test
    fun `parses a minimal inline example`() {
        val json = """[{"label":"TEST 16GB","note":null,"codes":["ABC123","DEF456"]}]"""

        val categories = CapacityListJsonParser.parse(json)

        assertEquals(1, categories.size)
        assertEquals("TEST 16GB", categories.first().label)
        assertEquals(listOf("ABC123", "DEF456"), categories.first().codes)
    }
}
