package io.github.hosseinkarami_dev.focusai

import android.content.Context
import android.os.Build
import androidx.room3.Room
import androidx.room3.RoomDatabase
import io.github.hosseinkarami_dev.focusai.data.FocusDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun formatEpochDate(epochMillis: Long, language: String): String =
    SimpleDateFormat("MMM d, yyyy", Locale.forLanguageTag(language)).format(Date(epochMillis))

actual fun createDatabaseBuilder(context: Any?): RoomDatabase.Builder<FocusDatabase> {
    val appContext = (context as Context).applicationContext
    val file = appContext.getDatabasePath("focusai.db")
    return Room.databaseBuilder<FocusDatabase>(context = appContext, name = file.absolutePath)
}
