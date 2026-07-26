package com.gentech.chipcheck.ui.result

import com.gentech.chipcheck.domain.decoder.DecodedChip

object ShareTextBuilder {

    fun build(chip: DecodedChip): String {
        fun segmentText(name: String): String {
            val segment = chip.segments.firstOrNull { it.name == name } ?: return "N/A"
            return segment.decodedValue ?: "Unknown (${segment.rawValue})"
        }

        return buildString {
            appendLine("Chip check result — GeNTech Repairs")
            appendLine("Part No: ${chip.normalizedPartNumber}")
            appendLine("Vendor: ${chip.vendor ?: "Unknown"}")
            appendLine("Type: ${segmentText("type")}")
            appendLine("Package: ${segmentText("package")}")
            appendLine("Storage: ${segmentText("storage")}")
            appendLine("RAM: ${segmentText("ram")}")
            appendLine()
            appendLine("Available na po ba replacement nito? Message lang:")
            append("m.me/genesiscruz0124")
        }
    }
}
