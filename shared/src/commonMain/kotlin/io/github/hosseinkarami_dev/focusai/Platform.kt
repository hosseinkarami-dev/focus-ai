package io.github.hosseinkarami_dev.focusai

import androidx.room3.RoomDatabase
import io.github.hosseinkarami_dev.focusai.data.FocusDatabase

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun currentTimeMillis(): Long
expect fun createDatabaseBuilder(context: Any? = null): RoomDatabase.Builder<FocusDatabase>
