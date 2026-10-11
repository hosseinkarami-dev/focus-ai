package io.github.hosseinkarami_dev.focusai.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.hosseinkarami_dev.focusai.data.FocusSessionEntity
import io.github.hosseinkarami_dev.focusai.data.FocusSessionRepository
import io.github.hosseinkarami_dev.focusai.data.TaskEntity
import io.github.hosseinkarami_dev.focusai.data.TaskRepository
import io.github.hosseinkarami_dev.focusai.FocusBreakReminderScheduler
import io.github.hosseinkarami_dev.focusai.localDateIso
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FocusViewModel(private val repository: FocusSessionRepository, tasks: TaskRepository, private val breakReminderScheduler: FocusBreakReminderScheduler) : ViewModel() {
    val active = repository.observeActive().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val history = repository.observeHistory().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val allSessions = repository.observeAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val tasks = tasks.observeTasks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun start(minutes: Int, taskId: String?) = viewModelScope.launch { repository.start(minutes, taskId) }
    fun pause() = viewModelScope.launch { repository.pause() }
    fun resume() = viewModelScope.launch { repository.resume() }
    fun finish(sendBreakReminder: Boolean, title: String, message: String) = viewModelScope.launch {
        if (repository.finish() && sendBreakReminder) breakReminderScheduler.notifyBreak(title, message)
    }
    fun discard() = viewModelScope.launch { repository.discard() }
}

fun FocusSessionEntity.elapsedMillis(nowEpochMillis: Long): Long =
    (accumulatedRunningMillis + (lastResumedAtEpochMillis?.let { (nowEpochMillis - it).coerceAtLeast(0) } ?: 0L)).coerceAtLeast(0)

data class FocusInsights(
    val completedTasks: Int,
    val completedSessions: Int,
    val focusMinutes: Long,
    val averageSessionMinutes: Long,
    val sessionTrend: List<Int>,
    val hasUntrackedLegacyCompletions: Boolean
)

fun todayFocusMinutes(
    sessions: List<FocusSessionEntity>,
    activeSession: FocusSessionEntity?,
    nowEpochMillis: Long
): Long {
    val today = localDateIso(nowEpochMillis)
    val completedTodayMillis = sessions.asSequence()
        .filter { it.outcome == "COMPLETED" && it.endedAtEpochMillis?.let { ended -> localDateIso(ended) == today } == true }
        .sumOf { it.accumulatedRunningMillis }
    val pausedTodayMillis = sessions.asSequence()
        .filter { it.outcome == "PAUSED" && localDateIso(it.startedAtEpochMillis) == today }
        .sumOf { it.accumulatedRunningMillis }
    val activeTodayMillis = activeSession
        ?.takeIf { it.lastResumedAtEpochMillis != null && localDateIso(it.startedAtEpochMillis) == today }
        ?.elapsedMillis(nowEpochMillis)
        ?: 0L
    return (completedTodayMillis + pausedTodayMillis + activeTodayMillis).coerceAtLeast(0L) / 60_000L
}

fun deriveFocusInsights(
    tasks: List<TaskEntity>,
    sessions: List<FocusSessionEntity>,
    nowEpochMillis: Long,
    periodDays: Int
): FocusInsights {
    val windowMillis = periodDays.coerceIn(1, 365) * 24L * 60L * 60L * 1_000L
    val start = nowEpochMillis - windowMillis
    val completedTasks = tasks.count { task ->
        task.completedAtEpochMillis?.let { it in start..nowEpochMillis } == true
    }
    val completedSessions = sessions.filter { session ->
        session.outcome == "COMPLETED" && session.endedAtEpochMillis?.let { it in start..nowEpochMillis } == true
    }
    val bucketSize = windowMillis / 7
    val trend = List(7) { bucket ->
        val bucketStart = start + bucket * bucketSize
        val bucketEnd = if (bucket == 6) nowEpochMillis else bucketStart + bucketSize
        completedSessions.count { session ->
            session.endedAtEpochMillis?.let { it >= bucketStart && if (bucket == 6) it <= bucketEnd else it < bucketEnd } == true
        }
    }
    return FocusInsights(
        completedTasks = completedTasks,
        completedSessions = completedSessions.size,
        focusMinutes = completedSessions.sumOf { it.accumulatedRunningMillis }.coerceAtLeast(0L) / 60_000L,
        averageSessionMinutes = if (completedSessions.isEmpty()) 0 else completedSessions.sumOf { it.accumulatedRunningMillis }.coerceAtLeast(0L) / completedSessions.size / 60_000L,
        sessionTrend = trend,
        hasUntrackedLegacyCompletions = tasks.any { it.completed && it.completedAtEpochMillis == null }
    )
}
