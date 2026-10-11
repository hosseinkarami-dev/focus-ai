package io.github.hosseinkarami_dev.focusai

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import io.github.hosseinkarami_dev.focusai.data.loadWidgetTaskTitles
import io.github.hosseinkarami_dev.focusai.di.initKoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FocusAiWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                initKoin(context.applicationContext)
                val titles = loadWidgetTaskTitles()
                appWidgetIds.forEach { id -> manager.updateAppWidget(id, widgetViews(context, titles)) }
            } finally {
                pendingResult.finish()
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_REFRESH) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, FocusAiWidgetProvider::class.java))
            onUpdate(context, manager, ids)
        }
    }

    companion object {
        const val ACTION_REFRESH = "io.github.hosseinkarami_dev.focusai.REFRESH_WIDGET"

        fun refresh(context: Context) {
            context.sendBroadcast(Intent(context, FocusAiWidgetProvider::class.java).setAction(ACTION_REFRESH))
        }
    }
}

private fun widgetViews(context: Context, taskTitles: List<String>): RemoteViews =
    RemoteViews(context.packageName, R.layout.focusai_widget).apply {
        setTextViewText(R.id.widget_task_list, if (taskTitles.isEmpty()) context.getString(R.string.widget_empty) else taskTitles.joinToString("\n") { "•  $it" })
        setOnClickPendingIntent(R.id.widget_add_task, launchPendingIntent(context, ACTION_ADD_TASK, 0))
        setOnClickPendingIntent(R.id.widget_capture, launchPendingIntent(context, ACTION_CAPTURE_NOTE, 1))
        setOnClickPendingIntent(R.id.widget_open, launchPendingIntent(context, null, 2))
    }

private fun launchPendingIntent(context: Context, action: String?, requestCode: Int): PendingIntent {
    val intent = Intent(context, MainActivity::class.java).apply {
        this.action = action
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    }
    return PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
}

private const val ACTION_ADD_TASK = "io.github.hosseinkarami_dev.focusai.ADD_TASK"
private const val ACTION_CAPTURE_NOTE = "io.github.hosseinkarami_dev.focusai.CAPTURE_NOTE"
