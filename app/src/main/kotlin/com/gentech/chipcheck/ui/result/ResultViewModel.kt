package com.gentech.chipcheck.ui.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gentech.chipcheck.domain.decoder.ChipDecoder
import com.gentech.chipcheck.domain.decoder.DecodedChip
import com.gentech.chipcheck.domain.decoder.DecodedSegment
import com.gentech.chipcheck.domain.repository.ChipRuleRepository
import com.gentech.chipcheck.domain.repository.ScanHistoryRepository
import com.gentech.chipcheck.domain.repository.ScanSource
import com.gentech.chipcheck.domain.repository.UnmappedCodeRepository
import com.gentech.chipcheck.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResultUiState(
    val decodedChip: DecodedChip? = null,
    val source: ScanSource = ScanSource.MANUAL,
    val isSaved: Boolean = false,
    val reportedSegmentNames: Set<String> = emptySet()
)

@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chipRuleRepository: ChipRuleRepository,
    private val chipDecoder: ChipDecoder,
    private val scanHistoryRepository: ScanHistoryRepository,
    private val unmappedCodeRepository: UnmappedCodeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    init {
        val historyId = savedStateHandle.get<String>(Routes.RESULT_HISTORY_ID_ARG)?.toLongOrNull()
        val partNumber = savedStateHandle.get<String>(Routes.RESULT_PART_NUMBER_ARG)
        val source = savedStateHandle.get<String>(Routes.RESULT_SOURCE_ARG)
            ?.let { runCatching { ScanSource.valueOf(it) }.getOrNull() }
            ?: ScanSource.MANUAL

        viewModelScope.launch {
            when {
                historyId != null -> {
                    scanHistoryRepository.getById(historyId)?.let { item ->
                        _uiState.value = ResultUiState(
                            decodedChip = item.decodedChip,
                            source = item.source,
                            isSaved = true
                        )
                    }
                }

                partNumber != null -> {
                    val rules = chipRuleRepository.getRules()
                    val chip = chipDecoder.decode(partNumber, rules)
                    _uiState.value = ResultUiState(decodedChip = chip, source = source, isSaved = false)
                }
            }
        }
    }

    fun saveToHistory() {
        val state = _uiState.value
        val chip = state.decodedChip ?: return
        if (state.isSaved) return

        viewModelScope.launch {
            scanHistoryRepository.save(chip, state.source)
            _uiState.value = state.copy(isSaved = true)
        }
    }

    fun reportUnknownSegment(segment: DecodedSegment) {
        val chip = _uiState.value.decodedChip ?: return

        viewModelScope.launch {
            unmappedCodeRepository.submit(
                vendorGuess = chip.vendor,
                segmentName = segment.name,
                rawCode = segment.rawValue,
                fullPartNumber = chip.normalizedPartNumber
            )
            _uiState.value = _uiState.value.copy(
                reportedSegmentNames = _uiState.value.reportedSegmentNames + segment.name
            )
        }
    }

    fun shareText(): String? = _uiState.value.decodedChip?.let { ShareTextBuilder.build(it) }
}
