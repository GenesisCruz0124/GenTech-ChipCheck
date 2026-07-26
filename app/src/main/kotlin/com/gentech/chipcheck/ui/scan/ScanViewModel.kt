package com.gentech.chipcheck.ui.scan

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ScanViewModel @Inject constructor() : ViewModel() {

    private val _detectedLines = MutableStateFlow<List<String>>(emptyList())
    val detectedLines: StateFlow<List<String>> = _detectedLines.asStateFlow()

    /** True once a picked-photo result is being shown, so live camera frames stop overwriting it. */
    private val _isReviewingPhoto = MutableStateFlow(false)
    val isReviewingPhoto: StateFlow<Boolean> = _isReviewingPhoto.asStateFlow()

    /** Called continuously by the live CameraX analyzer -- ignored while reviewing a photo. */
    fun onLiveCameraLinesDetected(lines: List<String>) {
        if (_isReviewingPhoto.value) return
        _detectedLines.value = lines
    }

    /** Called once after a user picks a gallery photo and OCR finishes on it. */
    fun onPhotoLinesDetected(lines: List<String>) {
        _isReviewingPhoto.value = true
        _detectedLines.value = lines
    }

    fun resumeLiveScanning() {
        _isReviewingPhoto.value = false
        _detectedLines.value = emptyList()
    }
}
