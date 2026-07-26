package com.gentech.chipcheck.ui.scan

import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

/**
 * Single shared instance of the bundled (on-device) ML Kit text recognizer -- NOT the Play
 * Services / unbundled variant, which can require a network model download on first use and
 * would violate the app's offline requirement. Shared by both the live camera analyzer and
 * the gallery-photo recognition path so there's one client and one line-extraction rule.
 */
object BundledTextRecognizerClient {
    val client: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun extractLines(text: Text): List<String> =
        text.textBlocks
            .flatMap { it.lines }
            .map { it.text.trim() }
            .filter { it.isNotBlank() }
}
