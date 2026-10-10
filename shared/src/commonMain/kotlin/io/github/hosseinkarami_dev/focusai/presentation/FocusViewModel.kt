package io.github.hosseinkarami_dev.focusai.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.hosseinkarami_dev.focusai.data.FocusSessionEntity
import io.github.hosseinkarami_dev.focusai.data.FocusSessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FocusViewModel(private val repository: FocusSessionRepository) : ViewModel() {
    val active = repository.observeActive().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val history = repository.observeHistory().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun start(minutes: Int, taskId: String?) = viewModelScope.launch { repository.start(minutes, taskId) }
    fun pause() = viewModelScope.launch { repository.pause() }
    fun resume() = viewModelScope.launch { repository.resume() }
    fun finish() = viewModelScope.launch { repository.finish() }
    fun discard() = viewModelScope.launch { repository.discard() }
}

fun FocusSessionEntity.elapsedMillis(nowEpochMillis: Long): Long =
    (accumulatedRunningMillis + (lastResumedAtEpochMillis?.let { (nowEpochMillis - it).coerceAtLeast(0) } ?: 0L)).coerceAtLeast(0)
