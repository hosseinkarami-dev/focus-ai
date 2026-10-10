package io.github.hosseinkarami_dev.focusai

import androidx.room3.RoomDatabase
import io.github.hosseinkarami_dev.focusai.data.FocusDatabase

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

fun currentTimeMillis(): Long = kotlin.time.Clock.System.now().toEpochMilliseconds()
expect fun formatEpochDate(epochMillis: Long, language: String = "en"): String
expect fun createDatabaseBuilder(context: Any? = null): RoomDatabase.Builder<FocusDatabase>
