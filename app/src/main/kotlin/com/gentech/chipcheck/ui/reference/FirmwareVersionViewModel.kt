package com.gentech.chipcheck.ui.reference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gentech.chipcheck.domain.repository.FirmwareVersionEntry
import com.gentech.chipcheck.domain.repository.FirmwareVersionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FirmwareVersionUiState(
    val query: String = "",
    val allEntries: List<FirmwareVersionEntry> = emptyList()
) {
    val filteredEntries: List<FirmwareVersionEntry>
        get() = if (query.isBlank()) {
            allEntries
        } else {
            allEntries.filter { it.model.contains(query, ignoreCase = true) }
        }
}

@HiltViewModel
class FirmwareVersionViewModel @Inject constructor(
    private val repository: FirmwareVersionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FirmwareVersionUiState())
    val uiState: StateFlow<FirmwareVersionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val entries = repository.getEntries()
            _uiState.value = _uiState.value.copy(allEntries = entries)
        }
    }

    fun onQueryChanged(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
    }
}
