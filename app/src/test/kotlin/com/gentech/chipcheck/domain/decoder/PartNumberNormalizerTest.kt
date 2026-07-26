package com.gentech.chipcheck.domain.decoder

import org.junit.Assert.assertEquals
import org.junit.Test

class PartNumberNormalizerTest {

    @Test
    fun `lowercase input is uppercased`() {
        assertEquals("H9TQ17ADFTMC", PartNumberNormalizer.normalize("h9tq17adftmc"))
    }

    @Test
    fun `spaces and dashes are stripped`() {
        assertEquals("H9TQ17ADFTMC", PartNumberNormalizer.normalize("H9 TQ-17-ADFTMC"))
    }

    @Test
    fun `mixed case with spaces and dashes normalizes correctly`() {
        assertEquals("H9TQ17ADFTMC", PartNumberNormalizer.normalize(" h9-tq 17-ad ftmc "))
    }
}
