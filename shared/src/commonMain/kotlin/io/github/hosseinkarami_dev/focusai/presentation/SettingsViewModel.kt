package io.github.hosseinkarami_dev.focusai.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.hosseinkarami_dev.focusai.data.AppSettingsEntity
import io.github.hosseinkarami_dev.focusai.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    val settings: StateFlow<AppSettingsEntity> = repository.observeSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppSettingsEntity())

    fun setThemeMode(mode: String) = viewModelScope.launch { repository.setThemeMode(mode) }

    fun setFocusLength(minutes: Int) = viewModelScope.launch { repository.setFocusLength(minutes) }

    fun setLanguage(language: String) = viewModelScope.launch { repository.setLanguage(language) }
}
