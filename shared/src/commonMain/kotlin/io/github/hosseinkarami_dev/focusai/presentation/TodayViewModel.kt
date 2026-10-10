package io.github.hosseinkarami_dev.focusai.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.hosseinkarami_dev.focusai.data.AiProvider
import io.github.hosseinkarami_dev.focusai.data.TaskEntity
import io.github.hosseinkarami_dev.focusai.data.TaskProposal
import io.github.hosseinkarami_dev.focusai.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TodayUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val note: String = "",
    val proposals: List<TaskProposal> = emptyList(),
    val analyzing: Boolean = false
)

class TodayViewModel(
    private val tasks: TaskRepository,
    private val aiProvider: AiProvider
) : ViewModel() {
    val taskList: StateFlow<List<TaskEntity>> = tasks.observeTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addTask(title: String) = viewModelScope.launch {
        if (title.isNotBlank()) tasks.addTask(title)
    }

    fun toggle(task: TaskEntity) = viewModelScope.launch {
        tasks.setCompleted(task, !task.completed)
    }

    suspend fun analyze(note: String): List<TaskProposal> = aiProvider.extractTasks(note)

    fun confirm(proposals: List<TaskProposal>) = viewModelScope.launch {
        proposals.forEach { tasks.addTask(it.title, priority = it.priority, estimateMinutes = it.estimateMinutes) }
    }
}
