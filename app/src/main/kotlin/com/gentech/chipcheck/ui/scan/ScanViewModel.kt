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

    fun onLinesDetected(lines: List<String>) {
        _detectedLines.value = lines
    }
}
