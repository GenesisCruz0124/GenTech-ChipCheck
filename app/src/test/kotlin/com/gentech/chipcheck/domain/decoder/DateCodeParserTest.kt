package com.gentech.chipcheck.domain.decoder

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DateCodeParserTest {

    @Test
    fun `trailing four digits produce both YYWW and WWYY readings`() {
        val hint = DateCodeParser.parse("H9TQ17ADFTMC2419")

        assertEquals("2419", hint?.rawDigits)
        assertEquals("2024, Week 19", hint?.yywwReading)
        assertEquals("Week 24, 2019", hint?.wwyyReading)
    }

    @Test
    fun `input shorter than four digits produces no hint`() {
        assertNull(DateCodeParser.parse("A1"))
    }

    @Test
    fun `input without a trailing digit run produces no hint`() {
        assertNull(DateCodeParser.parse("H9TQ17ADFTMC"))
    }
}
