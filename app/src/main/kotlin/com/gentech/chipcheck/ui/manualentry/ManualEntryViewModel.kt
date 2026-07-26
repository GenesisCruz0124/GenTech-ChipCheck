package com.gentech.chipcheck.ui.manualentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gentech.chipcheck.domain.decoder.ChipDecoder
import com.gentech.chipcheck.domain.decoder.DecodedChip
import com.gentech.chipcheck.domain.decoder.PartNumberNormalizer
import com.gentech.chipcheck.domain.repository.ChipRuleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val LIVE_DECODE_DEBOUNCE_MS = 300L

@HiltViewModel
class ManualEntryViewModel @Inject constructor(
    private val chipRuleRepository: ChipRuleRepository,
    private val chipDecoder: ChipDecoder
) : ViewModel() {

    private val _input = MutableStateFlow("")
    val input: StateFlow<String> = _input.asStateFlow()

    private val _decodedChip = MutableStateFlow<DecodedChip?>(null)
    val decodedChip: StateFlow<DecodedChip?> = _decodedChip.asStateFlow()

    private var debounceJob: Job? = null

    fun onInputChanged(raw: String) {
        val normalized = PartNumberNormalizer.normalize(raw)
        _input.value = normalized
        debounceJob?.cancel()

        if (normalized.isBlank()) {
            _decodedChip.value = null
            return
        }

        debounceJob = viewModelScope.launch {
            delay(LIVE_DECODE_DEBOUNCE_MS)
            val rules = chipRuleRepository.getRules()
            _decodedChip.value = chipDecoder.decode(normalized, rules)
        }
    }
}
