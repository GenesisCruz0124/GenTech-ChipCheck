package com.gentech.chipcheck.ui.reference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gentech.chipcheck.domain.repository.CapacityCategory
import com.gentech.chipcheck.domain.repository.CapacityListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CapacityListUiState(
    val categories: List<CapacityCategory> = emptyList(),
    val selectedLabel: String? = null
)

@HiltViewModel
class CapacityListViewModel @Inject constructor(
    private val repository: CapacityListRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CapacityListUiState())
    val uiState: StateFlow<CapacityListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val categories = repository.getCategories()
            _uiState.value = CapacityListUiState(
                categories = categories,
                selectedLabel = categories.firstOrNull()?.label
            )
        }
    }

    fun selectCategory(label: String) {
        _uiState.value = _uiState.value.copy(selectedLabel = label)
    }
}
