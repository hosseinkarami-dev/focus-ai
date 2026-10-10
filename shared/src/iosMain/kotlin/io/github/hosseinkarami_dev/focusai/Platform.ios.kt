package io.github.hosseinkarami_dev.focusai

import androidx.room3.Room
import androidx.room3.RoomDatabase
import io.github.hosseinkarami_dev.focusai.data.FocusDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSDate
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000.0).toLong()

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
