package com.gentech.chipcheck.ui.result

import com.gentech.chipcheck.data.rules.ChipRuleJsonParser
import com.gentech.chipcheck.domain.decoder.ChipDecoder
import org.junit.Assert.assertEquals
import org.junit.Test

class ShareTextBuilderTest {

    @Test
    fun `SK hynix worked example produces the exact Taglish share template`() {
        val rawJson = requireNotNull(
            Thread.currentThread().contextClassLoader?.getResourceAsStream("chip_rules.json")
        ).bufferedReader().use { it.readText() }
        val rules = ChipRuleJsonParser.parse(rawJson)
        val chip = ChipDecoder().decode("H9TQ17ADFTMC", rules)

        val expected = """
            Chip check result — GeNTech Repairs
            Part No: H9TQ17ADFTMC
            Vendor: SK hynix
            Type: eMCP
            Package: BGA 221 / BGA 529
            Storage: 16 GB
            RAM: 3 GB

            Available na po ba replacement nito? Message lang:
            m.me/genesiscruz0124
        """.trimIndent()

        assertEquals(expected, ShareTextBuilder.build(chip))
    }
}
