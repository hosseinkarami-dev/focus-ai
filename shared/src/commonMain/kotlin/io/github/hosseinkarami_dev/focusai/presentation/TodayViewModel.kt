package io.github.hosseinkarami_dev.focusai.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.hosseinkarami_dev.focusai.data.AiProvider
import io.github.hosseinkarami_dev.focusai.data.TaskEntity
import io.github.hosseinkarami_dev.focusai.data.SubtaskEntity
import io.github.hosseinkarami_dev.focusai.data.TaskStepDraft
import io.github.hosseinkarami_dev.focusai.data.NoteEntity
import io.github.hosseinkarami_dev.focusai.data.NoteRepository
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
    private val notes: NoteRepository,
    private val aiProvider: AiProvider
) : ViewModel() {
    val taskList: StateFlow<List<TaskEntity>> = tasks.observeTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val noteList: StateFlow<List<NoteEntity>> = notes.observeNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    suspend fun saveTask(title: String, details: String, priority: String, estimateMinutes: Int, dueDateIso: String?, subtasks: List<TaskStepDraft>, existing: TaskEntity? = null) {
        if (title.isNotBlank()) tasks.saveTask(title, details, priority, estimateMinutes, dueDateIso, subtasks, existing)
    }

    suspend fun loadSubtasks(taskId: String): List<SubtaskEntity> = tasks.getSubtasks(taskId)

    fun deleteTask(task: TaskEntity) = viewModelScope.launch { tasks.deleteTask(task.id) }

    fun moveTask(task: TaskEntity, direction: Int) = viewModelScope.launch { tasks.moveTask(task.id, direction) }

    fun setCompleted(task: TaskEntity, completed: Boolean) = viewModelScope.launch {
        tasks.setCompleted(task, completed)
    }

    suspend fun analyze(note: String): List<TaskProposal> = aiProvider.extractTasks(note)

    suspend fun saveNote(body: String, existing: NoteEntity? = null): NoteEntity = notes.saveNote(body, existing)

    fun deleteNote(id: String) = viewModelScope.launch { notes.deleteNote(id) }

    fun setNoteArchived(id: String, archived: Boolean) = viewModelScope.launch { notes.setArchived(id, archived) }

    fun confirm(proposals: List<TaskProposal>, sourceNoteId: String? = null) = viewModelScope.launch {
        proposals.forEach { tasks.saveTask(it.title, priority = it.priority, estimateMinutes = it.estimateMinutes) }
        sourceNoteId?.let { notes.markProcessed(it) }
    }
}
