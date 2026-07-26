package com.gentech.chipcheck.data.rules

import com.gentech.chipcheck.domain.decoder.SegmentStrategy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ChipRuleJsonParserTest {

    private fun loadRealAsset(): String =
        requireNotNull(
            Thread.currentThread().contextClassLoader?.getResourceAsStream("chip_rules.json")
        ).bufferedReader().use { it.readText() }

    @Test
    fun `parses all five seeded vendors from the real shipping asset`() {
        val rules = ChipRuleJsonParser.parse(loadRealAsset())

        val vendors = rules.map { it.vendor }
        assertEquals(
            listOf("SK hynix", "Samsung", "Micron", "Kioxia / Toshiba", "YMTC"),
            vendors
        )
    }

    @Test
    fun `SK hynix rule has four fixed-length segments after the type prefix segment`() {
        val rules = ChipRuleJsonParser.parse(loadRealAsset())
        val hynix = rules.first { it.vendor == "SK hynix" }

        assertEquals(4, hynix.segments.size)
        assertTrue(hynix.segments[0].strategy is SegmentStrategy.Prefix)
        assertTrue(hynix.segments[1].strategy is SegmentStrategy.FixedLength)
        assertEquals(2, (hynix.segments[1].strategy as SegmentStrategy.FixedLength).length)
    }

    @Test
    fun `unknown strategy string throws a clear error`() {
        val badJson = """
            [{"vendor":"Bad","matchPrefixes":["BB"],"example":"BB01","segments":[
              {"name":"x","label":"X","strategy":"nonsense","map":{}}
            ]}]
        """.trimIndent()

        try {
            ChipRuleJsonParser.parse(badJson)
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("nonsense"))
        }
    }
}
