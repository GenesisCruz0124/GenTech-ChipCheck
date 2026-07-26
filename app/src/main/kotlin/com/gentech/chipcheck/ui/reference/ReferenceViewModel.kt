package com.gentech.chipcheck.ui.reference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gentech.chipcheck.domain.decoder.ChipRule
import com.gentech.chipcheck.domain.repository.ChipRuleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReferenceUiState(
    val vendors: List<ChipRule> = emptyList(),
    val selectedVendor: String? = null
)

@HiltViewModel
class ReferenceViewModel @Inject constructor(
    private val chipRuleRepository: ChipRuleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReferenceUiState())
    val uiState: StateFlow<ReferenceUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val rules = chipRuleRepository.getRules()
            _uiState.value = ReferenceUiState(vendors = rules, selectedVendor = rules.firstOrNull()?.vendor)
        }
    }

    fun selectVendor(vendor: String) {
        _uiState.value = _uiState.value.copy(selectedVendor = vendor)
    }
}
