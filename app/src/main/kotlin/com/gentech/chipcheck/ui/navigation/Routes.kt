package com.gentech.chipcheck.ui.navigation

import android.net.Uri
import com.gentech.chipcheck.domain.repository.ScanSource

object Routes {
    const val SCAN = "scan"
    const val MANUAL_ENTRY = "manual_entry"
    const val HISTORY = "history"
    const val REFERENCE = "reference"
    const val SETTINGS = "settings"

    const val RESULT_PART_NUMBER_ARG = "partNumber"
    const val RESULT_HISTORY_ID_ARG = "historyId"
    const val RESULT_SOURCE_ARG = "source"
    const val RESULT_ROUTE =
        "result?$RESULT_PART_NUMBER_ARG={$RESULT_PART_NUMBER_ARG}" +
            "&$RESULT_HISTORY_ID_ARG={$RESULT_HISTORY_ID_ARG}" +
            "&$RESULT_SOURCE_ARG={$RESULT_SOURCE_ARG}"

    fun resultForPartNumber(partNumber: String, source: ScanSource): String =
        "result?$RESULT_PART_NUMBER_ARG=${Uri.encode(partNumber)}&$RESULT_SOURCE_ARG=${source.name}"

    fun resultForHistoryId(historyId: Long): String =
        "result?$RESULT_HISTORY_ID_ARG=$historyId"
}
