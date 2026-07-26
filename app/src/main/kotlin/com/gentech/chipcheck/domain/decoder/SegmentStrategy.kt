package com.gentech.chipcheck.domain.decoder

sealed class SegmentStrategy {
    /** Decodes the vendor-selection prefix itself (e.g. the "type" segment on SK hynix). */
    data object Prefix : SegmentStrategy()

    /** Consumes the next [length] characters from the current cursor position. */
    data class FixedLength(val length: Int) : SegmentStrategy()
}
