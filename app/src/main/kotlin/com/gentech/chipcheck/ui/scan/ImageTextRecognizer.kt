package com.gentech.chipcheck.ui.scan

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/** Runs the same bundled/on-device OCR used for live camera frames against a picked photo. */
object ImageTextRecognizer {

    suspend fun recognizeLines(context: Context, uri: Uri): List<String> {
        val inputImage = InputImage.fromFilePath(context, uri)
        return suspendCancellableCoroutine { continuation ->
            BundledTextRecognizerClient.client.process(inputImage)
                .addOnSuccessListener { visionText ->
                    continuation.resume(BundledTextRecognizerClient.extractLines(visionText))
                }
                .addOnFailureListener { error -> continuation.resumeWithException(error) }
        }
    }
}
