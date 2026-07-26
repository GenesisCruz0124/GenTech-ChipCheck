package com.gentech.chipcheck.domain.decoder

import com.gentech.chipcheck.data.rules.ChipRuleJsonParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ChipDecoderTest {

    private lateinit var rules: List<ChipRule>
    private lateinit var decoder: ChipDecoder

    @Before
    fun setUp() {
        val rawJson = requireNotNull(
            Thread.currentThread().contextClassLoader?.getResourceAsStream("chip_rules.json")
        ).bufferedReader().use { it.readText() }
        rules = ChipRuleJsonParser.parse(rawJson)
        decoder = ChipDecoder()
    }

    @Test
    fun `SK hynix worked example decodes to eMCP BGA221-529 16GB storage 3GB RAM`() {
        val result = decoder.decode("H9TQ17ADFTMC", rules)

        assertEquals("SK hynix", result.vendor)
        assertEquals("eMCP", result.segmentValue("type"))
        assertEquals("BGA 221 / BGA 529", result.segmentValue("package"))
        assertEquals("16 GB", result.segmentValue("storage"))
        assertEquals("3 GB", result.segmentValue("ram"))
        assertEquals(Confidence.KNOWN, result.segmentByName("type")?.confidence)
        assertEquals(Confidence.KNOWN, result.segmentByName("package")?.confidence)
        assertEquals(Confidence.KNOWN, result.segmentByName("storage")?.confidence)
        assertEquals(Confidence.KNOWN, result.segmentByName("ram")?.confidence)
    }

    @Test
    fun `lowercase and spaced-dashed input normalizes to the same SK hynix result`() {
        val result = decoder.decode("h9 tq-17 ad-ft mc", rules)

        assertEquals("H9TQ17ADFTMC", result.normalizedPartNumber)
        assertEquals("SK hynix", result.vendor)
        assertEquals("16 GB", result.segmentValue("storage"))
    }

    @Test
    fun `unknown segment code within a matched vendor returns UNKNOWN with raw characters preserved`() {
        // "99" is not in the SK hynix storage map.
        val result = decoder.decode("H9TQ99ADFTMC", rules)

        val storage = result.segmentByName("storage")
        assertEquals("SK hynix", result.vendor)
        assertNull(storage?.decodedValue)
        assertEquals("99", storage?.rawValue)
        assertEquals(Confidence.UNKNOWN, storage?.confidence)
    }

    @Test
    fun `completely unrecognized prefix returns a single UNKNOWN segment for the whole input`() {
        val result = decoder.decode("ZZZ999NOPE", rules)

        assertNull(result.vendor)
        assertEquals(1, result.segments.size)
        assertEquals("ZZZ999NOPE", result.segments.first().rawValue)
        assertEquals(Confidence.UNKNOWN, result.segments.first().confidence)
    }

    @Test
    fun `Samsung K3 discrete LPDDR part decodes vendor and type but leaves the rest UNKNOWN`() {
        val result = decoder.decode("K3UH7H70BM", rules)

        assertEquals("Samsung", result.vendor)
        assertEquals("LPDDR (Discrete)", result.segmentValue("type"))
        val remaining = result.segmentByName("remaining")
        assertEquals("UH7H70BM", remaining?.rawValue)
        assertEquals(Confidence.UNKNOWN, remaining?.confidence)
    }

    @Test
    fun `Samsung KLU prefix wins over the shorter KL prefix for UFS parts`() {
        val result = decoder.decode("KLUEG4RHGB", rules)

        assertEquals("Samsung", result.vendor)
        assertEquals("UFS", result.segmentValue("type"))
        assertEquals("KLU", result.segmentByName("type")?.rawValue)
    }

    @Test
    fun `Micron vendor is identified with family-only segment`() {
        val result = decoder.decode("MT29F32G08CBACAWP", rules)

        assertEquals("Micron", result.vendor)
        assertEquals(Confidence.KNOWN, result.segmentByName("type")?.confidence)
        assertEquals(Confidence.UNKNOWN, result.segmentByName("remaining")?.confidence)
    }

    @Test
    fun `Kioxia Toshiba THG prefix is vendor-identified only`() {
        val result = decoder.decode("THGBMNG5D1LBAIL", rules)

        assertEquals("Kioxia / Toshiba", result.vendor)
        assertEquals(Confidence.KNOWN, result.segmentByName("type")?.confidence)
    }

    @Test
    fun `YMTC YM prefix is vendor-identified only`() {
        val result = decoder.decode("YMN08TB9902BE1", rules)

        assertEquals("YMTC", result.vendor)
        assertEquals(Confidence.KNOWN, result.segmentByName("type")?.confidence)
    }

    private fun DecodedChip.segmentByName(name: String) = segments.firstOrNull { it.name == name }
    private fun DecodedChip.segmentValue(name: String) = segmentByName(name)?.decodedValue
}
