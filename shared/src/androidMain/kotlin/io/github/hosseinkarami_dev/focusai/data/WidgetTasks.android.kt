package io.github.hosseinkarami_dev.focusai.data

import org.koin.core.context.GlobalContext

actual suspend fun loadWidgetTaskTitles(): List<String> =
    GlobalContext.get().get<FocusDatabase>().taskDao().getWidgetTasks().map { it.title }
