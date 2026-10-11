package io.github.hosseinkarami_dev.focusai

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.Manifest
import android.app.Notification
import android.content.Context
import android.provider.Settings
import io.github.hosseinkarami_dev.focusai.data.FocusDatabase
import io.github.hosseinkarami_dev.focusai.di.initKoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.room3.Room
import androidx.room3.RoomDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.text.ParsePosition

private const val TASK_REMINDER_CHANNEL = "task_reminders"
private const val FOCUS_BREAK_CHANNEL = "focus_breaks"

private class AndroidFocusBreakReminderScheduler(context: Context) : FocusBreakReminderScheduler {
    private val appContext = context.applicationContext

    override fun notifyBreak(title: String, message: String) {
        if (Build.VERSION.SDK_INT >= 33 && appContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        val manager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26 && manager.getNotificationChannel(FOCUS_BREAK_CHANNEL) == null) {
            manager.createNotificationChannel(NotificationChannel(FOCUS_BREAK_CHANNEL, "Focus breaks", NotificationManager.IMPORTANCE_DEFAULT))
        }
        val notification = if (Build.VERSION.SDK_INT >= 26) Notification.Builder(appContext, FOCUS_BREAK_CHANNEL) else {
            @Suppress("DEPRECATION") Notification.Builder(appContext)
        }.setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()
        manager.notify("focus-break".hashCode(), notification)
    }
}

private class AndroidTaskReminderScheduler(context: Context) : TaskReminderScheduler {
    private val appContext = context.applicationContext

    override suspend fun schedule(taskId: String, title: String, dueDateIso: String, timeLocal: String): Boolean {
        if (Build.VERSION.SDK_INT >= 33 && appContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return false
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT).apply { isLenient = false }
        val parsed = parser.parse("$dueDateIso $timeLocal", ParsePosition(0)) ?: return false
        val triggerAtMillis = parsed.time
        if (triggerAtMillis <= System.currentTimeMillis()) return false
        val manager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26 && manager.getNotificationChannel(TASK_REMINDER_CHANNEL) == null) {
            manager.createNotificationChannel(NotificationChannel(TASK_REMINDER_CHANNEL, "Task reminders", NotificationManager.IMPORTANCE_DEFAULT))
        }
        val intent = Intent(appContext, TaskReminderReceiver::class.java).apply {
            putExtra("taskId", taskId)
            putExtra("title", title)
        }
        val pendingIntent = PendingIntent.getBroadcast(appContext, taskId.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        (appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        return true
    }

    override suspend fun cancel(taskId: String) {
        val intent = Intent(appContext, TaskReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(appContext, taskId.hashCode(), intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)
        pendingIntent?.let { (appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(it); it.cancel() }
    }

    override fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, appContext.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appContext.startActivity(intent)
    }
}

class TaskReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 33 && context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        val notification = if (Build.VERSION.SDK_INT >= 26) {
            Notification.Builder(context, TASK_REMINDER_CHANNEL)
        } else {
            @Suppress("DEPRECATION") Notification.Builder(context)
        }.setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setContentTitle("FocusAI task reminder").setContentText(title).setAutoCancel(true).build()
        manager.notify(intent.getStringExtra("taskId")?.hashCode() ?: title.hashCode(), notification)
    }
}

class TaskReminderBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED && intent.action != Intent.ACTION_MY_PACKAGE_REPLACED) return
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val appContext = context.applicationContext
                initKoin(appContext)
                val database = GlobalContext.get().get<FocusDatabase>()
                val scheduler = createTaskReminderScheduler(appContext)
                database.taskDao().getOpenTasksWithReminders().forEach { task ->
                    val date = task.dueDateIso ?: return@forEach
                    val time = task.reminderTimeLocal ?: return@forEach
                    scheduler.schedule(task.id, task.title, date, time)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun formatEpochDate(epochMillis: Long, language: String): String =
    SimpleDateFormat("MMM d, yyyy", Locale.forLanguageTag(language)).format(Date(epochMillis))

actual fun currentLocalDateIso(): String = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(Date())

actual fun formatWeekdayDate(epochMillis: Long, language: String): String =
    SimpleDateFormat("EEE", Locale.forLanguageTag(language)).format(Date(epochMillis))

actual fun createDatabaseBuilder(context: Any?): RoomDatabase.Builder<FocusDatabase> {
    val appContext = (context as Context).applicationContext
    val file = appContext.getDatabasePath("focusai.db")
    return Room.databaseBuilder<FocusDatabase>(context = appContext, name = file.absolutePath)
}

actual fun createTaskReminderScheduler(context: Any?): TaskReminderScheduler = AndroidTaskReminderScheduler(context as Context)
actual fun createFocusBreakReminderScheduler(context: Any?): FocusBreakReminderScheduler = AndroidFocusBreakReminderScheduler(context as Context)

actual fun localDateIso(epochMillis: Long): String = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(Date(epochMillis))
actual fun publishWidgetTaskSnapshot(taskTitles: List<String>) = Unit

@Composable
actual fun rememberNotificationPermissionRequester(): (onResult: (Boolean) -> Unit) -> Unit {
    val context = LocalContext.current
    var resultCallback by remember { mutableStateOf<((Boolean) -> Unit)?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        resultCallback?.invoke(granted)
        resultCallback = null
    }
    return remember(context, launcher) {
        { onResult: (Boolean) -> Unit ->
            if (Build.VERSION.SDK_INT < 33 || context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                onResult(true)
            } else {
                resultCallback = onResult
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) = BackHandler(enabled = enabled, onBack = onBack)
