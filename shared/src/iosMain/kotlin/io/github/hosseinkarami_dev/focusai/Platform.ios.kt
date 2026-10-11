package io.github.hosseinkarami_dev.focusai

import androidx.room3.Room
import androidx.room3.RoomDatabase
import io.github.hosseinkarami_dev.focusai.data.FocusDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIDevice
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.Foundation.NSURL
import platform.Foundation.NSUserDefaults
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun formatEpochDate(epochMillis: Long, language: String): String {
    val formatter = NSDateFormatter().apply {
        locale = platform.Foundation.NSLocale(localeIdentifier = language)
        dateFormat = "MMM d, yyyy"
    }
    val secondsSinceReferenceDate = epochMillis.toDouble() / 1000.0 - 978_307_200.0
    return formatter.stringFromDate(NSDate(timeIntervalSinceReferenceDate = secondsSinceReferenceDate))
}

actual fun currentLocalDateIso(): String = NSDateFormatter().apply {
    locale = platform.Foundation.NSLocale(localeIdentifier = "en_US_POSIX")
    dateFormat = "yyyy-MM-dd"
}.stringFromDate(NSDate())

actual fun formatWeekdayDate(epochMillis: Long, language: String): String = NSDateFormatter().apply {
    locale = platform.Foundation.NSLocale(localeIdentifier = language)
    dateFormat = "EEE"
}.stringFromDate(NSDate(timeIntervalSinceReferenceDate = epochMillis.toDouble() / 1000.0 - 978_307_200.0))

@OptIn(ExperimentalForeignApi::class)
actual fun createDatabaseBuilder(context: Any?): RoomDatabase.Builder<FocusDatabase> {
    val directory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null
    )?.path ?: error("Could not find the app documents directory")
    return Room.databaseBuilder<FocusDatabase>(name = "$directory/focusai.db")
}

private class IosTaskReminderScheduler : TaskReminderScheduler {
    override suspend fun schedule(taskId: String, title: String, dueDateIso: String, timeLocal: String): Boolean {
        val formatter = NSDateFormatter().apply {
            locale = platform.Foundation.NSLocale(localeIdentifier = "en_US_POSIX")
            dateFormat = "yyyy-MM-dd HH:mm"
        }
        val date = formatter.dateFromString("$dueDateIso $timeLocal") ?: return false
        val seconds = date.timeIntervalSinceReferenceDate - NSDate().timeIntervalSinceReferenceDate
        if (seconds <= 0) return false
        val center = UNUserNotificationCenter.currentNotificationCenter()
        val allowed = suspendCancellableCoroutine { continuation ->
            center.requestAuthorizationWithOptions(
                UNAuthorizationOptionAlert or UNAuthorizationOptionBadge or UNAuthorizationOptionSound
            ) { granted, error -> continuation.resume(granted && error == null) }
        }
        if (!allowed) return false
        val content = UNMutableNotificationContent().apply {
            setTitle("FocusAI task reminder")
            setBody(title)
            setSound(platform.UserNotifications.UNNotificationSound.defaultSound())
        }
        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(seconds, repeats = false)
        val request = UNNotificationRequest.requestWithIdentifier(taskId, content, trigger)
        return suspendCancellableCoroutine { continuation ->
            center.addNotificationRequest(request) { error -> continuation.resume(error == null) }
        }
    }

    override suspend fun cancel(taskId: String) {
        UNUserNotificationCenter.currentNotificationCenter().removePendingNotificationRequestsWithIdentifiers(listOf(taskId))
        UNUserNotificationCenter.currentNotificationCenter().removeDeliveredNotificationsWithIdentifiers(listOf(taskId))
    }

    override fun openNotificationSettings() {
        NSURL.URLWithString(UIApplicationOpenSettingsURLString)?.let { UIApplication.sharedApplication.openURL(it) }
    }
}

actual fun createTaskReminderScheduler(context: Any?): TaskReminderScheduler = IosTaskReminderScheduler()

private class IosFocusBreakReminderScheduler : FocusBreakReminderScheduler {
    override fun notifyBreak(title: String, message: String) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
            setSound(platform.UserNotifications.UNNotificationSound.defaultSound())
        }
        val request = UNNotificationRequest.requestWithIdentifier(
            "focus-break-${currentTimeMillis()}",
            content,
            UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(1.0, repeats = false)
        )
        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) { _ -> }
    }
}

actual fun createFocusBreakReminderScheduler(context: Any?): FocusBreakReminderScheduler = IosFocusBreakReminderScheduler()

actual fun localDateIso(epochMillis: Long): String = NSDateFormatter().apply {
    locale = platform.Foundation.NSLocale(localeIdentifier = "en_US_POSIX")
    dateFormat = "yyyy-MM-dd"
}.stringFromDate(NSDate(timeIntervalSinceReferenceDate = epochMillis.toDouble() / 1000.0 - 978_307_200.0))

actual fun publishWidgetTaskSnapshot(taskTitles: List<String>) {
    NSUserDefaults(suiteName = "group.io.github.hosseinkarami_dev.focusai")
        ?.setObject(taskTitles.joinToString("\n"), forKey = "focusai.openTaskTitles")
}

@Composable
actual fun rememberNotificationPermissionRequester(): (onResult: (Boolean) -> Unit) -> Unit = remember {
    { onResult ->
        UNUserNotificationCenter.currentNotificationCenter().requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionBadge or UNAuthorizationOptionSound
        ) { granted, error -> onResult(granted && error == null) }
    }
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) = Unit
