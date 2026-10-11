package io.github.hosseinkarami_dev.focusai.di

import io.github.hosseinkarami_dev.focusai.data.AiProvider
import io.github.hosseinkarami_dev.focusai.data.FocusDatabase
import io.github.hosseinkarami_dev.focusai.data.RoomTaskRepository
import io.github.hosseinkarami_dev.focusai.data.RoomNoteRepository
import io.github.hosseinkarami_dev.focusai.data.RoomSettingsRepository
import io.github.hosseinkarami_dev.focusai.data.RoomFocusSessionRepository
import io.github.hosseinkarami_dev.focusai.data.FocusSessionRepository
import io.github.hosseinkarami_dev.focusai.data.NoteRepository
import io.github.hosseinkarami_dev.focusai.data.SettingsRepository
import io.github.hosseinkarami_dev.focusai.data.SampleAiProvider
import io.github.hosseinkarami_dev.focusai.data.TaskRepository
import io.github.hosseinkarami_dev.focusai.data.createFocusDatabase
import io.github.hosseinkarami_dev.focusai.data.createHttpClient
import io.github.hosseinkarami_dev.focusai.createDatabaseBuilder
import io.github.hosseinkarami_dev.focusai.createTaskReminderScheduler
import io.github.hosseinkarami_dev.focusai.TaskReminderScheduler
import io.github.hosseinkarami_dev.focusai.FocusBreakReminderScheduler
import io.github.hosseinkarami_dev.focusai.createFocusBreakReminderScheduler
import io.github.hosseinkarami_dev.focusai.presentation.TodayViewModel
import io.github.hosseinkarami_dev.focusai.presentation.SettingsViewModel
import io.github.hosseinkarami_dev.focusai.presentation.FocusViewModel
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.dsl.module

private var koinStarted = false

fun initKoin(platformContext: Any? = null) {
    if (koinStarted) return
    val databaseBuilder = createDatabaseBuilder(platformContext)
    startKoin {
        modules(module {
            single { createFocusDatabase(databaseBuilder) }
            single<TaskReminderScheduler> { createTaskReminderScheduler(platformContext) }
            single<FocusBreakReminderScheduler> { createFocusBreakReminderScheduler(platformContext) }
            single { get<FocusDatabase>().taskDao() }
            single { get<FocusDatabase>().subtaskDao() }
            single { get<FocusDatabase>().noteDao() }
            single { get<FocusDatabase>().settingsDao() }
            single { get<FocusDatabase>().focusSessionDao() }
            single<TaskRepository> { RoomTaskRepository(get(), get(), get()) }
            single<NoteRepository> { RoomNoteRepository(get()) }
            single<SettingsRepository> { RoomSettingsRepository(get()) }
            single<FocusSessionRepository> { RoomFocusSessionRepository(get()) }
            single<AiProvider> { SampleAiProvider() }
            single<HttpClient> { createHttpClient() }
            factory { TodayViewModel(get(), get(), get(), get()) }
            factory { SettingsViewModel(get()) }
            factory { FocusViewModel(get(), get(), get()) }
        })
    }
    koinStarted = true
}
