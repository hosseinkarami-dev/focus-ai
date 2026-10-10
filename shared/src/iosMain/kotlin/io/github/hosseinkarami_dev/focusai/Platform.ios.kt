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
import kotlinx.cinterop.ExperimentalForeignApi

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
