package io.github.hosseinkarami_dev.focusai.di

import androidx.room3.RoomDatabase
import io.github.hosseinkarami_dev.focusai.data.AiProvider
import io.github.hosseinkarami_dev.focusai.data.FocusDatabase
import io.github.hosseinkarami_dev.focusai.data.RoomTaskRepository
import io.github.hosseinkarami_dev.focusai.data.SampleAiProvider
import io.github.hosseinkarami_dev.focusai.data.TaskRepository
import io.github.hosseinkarami_dev.focusai.data.createFocusDatabase
import io.github.hosseinkarami_dev.focusai.data.createHttpClient
import io.github.hosseinkarami_dev.focusai.presentation.TodayViewModel
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(databaseBuilder: RoomDatabase.Builder<FocusDatabase>) {
    startKoin {
        modules(module {
            single { createFocusDatabase(databaseBuilder) }
            single { get<FocusDatabase>().taskDao() }
            single<TaskRepository> { RoomTaskRepository(get()) }
            single<AiProvider> { SampleAiProvider() }
            single<HttpClient> { createHttpClient() }
            factory { TodayViewModel(get(), get()) }
        })
    }
}
