package io.github.hosseinkarami_dev.focusai

import androidx.room3.RoomDatabase
import androidx.compose.runtime.Composable
import io.github.hosseinkarami_dev.focusai.data.FocusDatabase

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

fun currentTimeMillis(): Long = kotlin.time.Clock.System.now().toEpochMilliseconds()
expect fun formatEpochDate(epochMillis: Long, language: String = "en"): String
expect fun currentLocalDateIso(): String
expect fun formatWeekdayDate(epochMillis: Long, language: String = "en"): String
expect fun createDatabaseBuilder(context: Any? = null): RoomDatabase.Builder<FocusDatabase>

interface TaskReminderScheduler {
    suspend fun schedule(taskId: String, title: String, dueDateIso: String, timeLocal: String): Boolean
    suspend fun cancel(taskId: String)
    fun openNotificationSettings()
}

interface FocusBreakReminderScheduler {
    fun notifyBreak(title: String, message: String)
}

expect fun createTaskReminderScheduler(context: Any? = null): TaskReminderScheduler
expect fun createFocusBreakReminderScheduler(context: Any? = null): FocusBreakReminderScheduler
expect fun localDateIso(epochMillis: Long): String
expect fun publishWidgetTaskSnapshot(taskTitles: List<String>)

@Composable
expect fun rememberNotificationPermissionRequester(): (onResult: (Boolean) -> Unit) -> Unit

@Composable
expect fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit)
