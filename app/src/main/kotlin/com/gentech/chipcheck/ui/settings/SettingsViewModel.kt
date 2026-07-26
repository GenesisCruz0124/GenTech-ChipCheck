package com.gentech.chipcheck.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gentech.chipcheck.domain.repository.ScanHistoryRepository
import com.gentech.chipcheck.domain.repository.ThemeMode
import com.gentech.chipcheck.domain.repository.UnmappedCodeItem
import com.gentech.chipcheck.domain.repository.UnmappedCodeRepository
import com.gentech.chipcheck.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val scanHistoryRepository: ScanHistoryRepository,
    private val unmappedCodeRepository: UnmappedCodeRepository
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = userPreferencesRepository.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)

    val unmappedCodes: StateFlow<List<UnmappedCodeItem>> = unmappedCodeRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { userPreferencesRepository.setThemeMode(mode) }
    }

    fun clearHistory() {
        viewModelScope.launch { scanHistoryRepository.clearAll() }
    }

    fun markUnmappedCodeReviewed(id: Long, reviewed: Boolean) {
        viewModelScope.launch { unmappedCodeRepository.markReviewed(id, reviewed) }
    }

    fun deleteUnmappedCode(id: Long) {
        viewModelScope.launch { unmappedCodeRepository.delete(id) }
    }
}
