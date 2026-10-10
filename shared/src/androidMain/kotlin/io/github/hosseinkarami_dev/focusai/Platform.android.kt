package io.github.hosseinkarami_dev.focusai

import android.content.Context
import android.os.Build
import androidx.room3.Room
import androidx.room3.RoomDatabase
import io.github.hosseinkarami_dev.focusai.data.FocusDatabase
import androidx.room3.RoomDatabase

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun currentTimeMillis(): Long = System.currentTimeMillis()

actual fun createDatabaseBuilder(context: Any?): RoomDatabase.Builder<FocusDatabase> {
    val appContext = (context as Context).applicationContext
    val file = appContext.getDatabasePath("focusai.db")
    return Room.databaseBuilder<FocusDatabase>(context = appContext, name = file.absolutePath)
}
