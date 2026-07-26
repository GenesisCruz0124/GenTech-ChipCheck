package com.gentech.chipcheck.ui.scan

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Wraps the shared bundled text recognizer for live CameraX frames. Frames are dropped while
 * a recognition is already in flight, and skipped entirely (frame closed, no OCR run) while
 * [isPaused] reports true -- e.g. while the user is reviewing a picked gallery photo's result.
 */
class TextRecognitionAnalyzer(
    private val onLinesDetected: (List<String>) -> Unit,
    private val isPaused: () -> Boolean = { false }
) : ImageAnalysis.Analyzer {

    private val isProcessing = AtomicBoolean(false)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (isPaused()) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage == null || !isProcessing.compareAndSet(false, true)) {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        BundledTextRecognizerClient.client.process(inputImage)
            .addOnSuccessListener { visionText ->
                val lines = BundledTextRecognizerClient.extractLines(visionText)
                if (lines.isNotEmpty()) {
                    onLinesDetected(lines)
                }
            }
            .addOnCompleteListener {
                isProcessing.set(false)
                imageProxy.close()
            }
    }
}
