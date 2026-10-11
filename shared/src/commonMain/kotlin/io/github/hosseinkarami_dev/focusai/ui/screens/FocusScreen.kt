package io.github.hosseinkarami_dev.focusai.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import io.github.hosseinkarami_dev.focusai.ui.FocusText
import io.github.hosseinkarami_dev.focusai.formatEpochDate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text as MaterialText
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import io.github.hosseinkarami_dev.focusai.currentTimeMillis
import io.github.hosseinkarami_dev.focusai.data.TaskEntity
import io.github.hosseinkarami_dev.focusai.presentation.elapsedMillis
import io.github.hosseinkarami_dev.focusai.presentation.todayFocusMinutes
import io.github.hosseinkarami_dev.focusai.ui.LocalAppLanguage
import io.github.hosseinkarami_dev.focusai.ui.localizedText

@Composable
internal fun FocusScreen(
    tasks: List<TaskEntity>, insightTasks: List<TaskEntity>, allSessions: List<io.github.hosseinkarami_dev.focusai.data.FocusSessionEntity>,
    padding: PaddingValues, focusLengthMinutes: Int, dailyFocusGoalMinutes: Int,
    activeSession: io.github.hosseinkarami_dev.focusai.data.FocusSessionEntity?,
    history: List<io.github.hosseinkarami_dev.focusai.data.FocusSessionEntity>,
    onStart: (Int, String?) -> Unit, onPause: () -> Unit, onResume: () -> Unit,
    onFinish: () -> Unit, onDiscard: () -> Unit
) {
    var now by remember { mutableStateOf(currentTimeMillis()) }
    var selectedTaskId by rememberSaveable { mutableStateOf<String?>(null) }
    var insightPeriodDays by rememberSaveable { mutableStateOf(7) }
    var confirmDiscard by remember { mutableStateOf(false) }
    LaunchedEffect(activeSession?.id, activeSession?.lastResumedAtEpochMillis) {
        while (true) {
            now = currentTimeMillis()
            kotlinx.coroutines.delay(if (activeSession?.lastResumedAtEpochMillis != null) 1_000 else 60_000)
        }
    }
    val elapsed = activeSession?.elapsedMillis(now) ?: 0L
    val planned = activeSession?.plannedDurationSeconds?.times(1_000L) ?: focusLengthMinutes * 60_000L
    val remaining = (planned - elapsed).coerceAtLeast(0L)
    val progress = if (planned > 0) (elapsed.toFloat() / planned).coerceIn(0f, 1f) else 0f
    val insights = remember(insightTasks, allSessions, now, insightPeriodDays) {
        io.github.hosseinkarami_dev.focusai.presentation.deriveFocusInsights(insightTasks, allSessions, now, insightPeriodDays)
    }
    val todayMinutes = todayFocusMinutes(allSessions, activeSession, now)
    LaunchedEffect(activeSession?.id, activeSession?.lastResumedAtEpochMillis, remaining <= 0L) {
        if (activeSession != null && activeSession.lastResumedAtEpochMillis != null && remaining <= 0L) onFinish()
    }
    Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        FocusText("Focus", modifier = Modifier.align(Alignment.Start), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        FocusText("A little room to do your best work.", modifier = Modifier.align(Alignment.Start), color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(26.dp))
        if (activeSession == null && tasks.isNotEmpty()) {
            FocusText("FOCUS ON A TASK", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary, letterSpacing = 1.sp)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = selectedTaskId == null, onClick = { selectedTaskId = null }, label = { FocusText("Just focus") })
                tasks.filterNot { it.completed }.forEach { task -> FilterChip(selected = selectedTaskId == task.id, onClick = { selectedTaskId = task.id }, label = { FocusText(task.title, maxLines = 1, overflow = TextOverflow.Ellipsis) }) }
            }
        }
        Spacer(Modifier.height(24.dp))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(250.dp)) {
            CircularProgressIndicator(progress = { if (activeSession == null) 0f else progress }, modifier = Modifier.fillMaxSize(), strokeWidth = 9.dp, color = MaterialTheme.colorScheme.primary, trackColor = MaterialTheme.colorScheme.primaryContainer)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FocusText(if (activeSession == null) formatTimer(planned) else formatTimer(remaining), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Medium)
                FocusText(when (activeSession?.outcome) { "PAUSED" -> "PAUSED"; null -> "MINDFUL MINUTES"; else -> if (remaining == 0L) "TIME TO FINISH" else "STAY WITH IT" }, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, letterSpacing = 1.1.sp)
            }
        }
        Spacer(Modifier.height(20.dp))
        val linkedTitle = activeSession?.taskId?.let { id -> tasks.firstOrNull { it.id == id }?.title }
        FocusText(linkedTitle ?: if (tasks.isEmpty()) "Take a breath, then begin." else "Your time, at your pace." , color = MaterialTheme.colorScheme.secondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(18.dp))
        if (activeSession == null) {
            Button(onClick = { onStart(focusLengthMinutes, selectedTaskId) }, shape = RoundedCornerShape(18.dp), contentPadding = PaddingValues(horizontal = 28.dp, vertical = 15.dp)) {
                Icon(Icons.Filled.PlayArrow, null); Spacer(Modifier.width(8.dp)); FocusText("Start focus", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { if (activeSession.lastResumedAtEpochMillis == null) onResume() else onPause() }, shape = RoundedCornerShape(16.dp)) {
                    Icon(if (activeSession.lastResumedAtEpochMillis == null) Icons.Filled.PlayArrow else Icons.Filled.Pause, contentDescription = null)
                    Spacer(Modifier.width(6.dp)); FocusText(if (activeSession.lastResumedAtEpochMillis == null) "Resume" else "Pause")
                }
                TextButton(onClick = onFinish) { FocusText("Finish") }
                TextButton(onClick = { confirmDiscard = true }) { FocusText("Discard", color = MaterialTheme.colorScheme.error) }
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        FocusText("Today's focus goal", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        FocusText("Build a steady rhythm, one session at a time.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                    FocusText("${(todayMinutes * 100 / dailyFocusGoalMinutes.coerceAtLeast(1)).coerceAtMost(100)}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                LinearProgressIndicator(
                    progress = { (todayMinutes.toFloat() / dailyFocusGoalMinutes.coerceAtLeast(1)).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surface
                )
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                    FocusText("$todayMinutes min", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                    FocusText("of", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                    FocusText("$dailyFocusGoalMinutes min", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
        Spacer(Modifier.height(30.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Column {
                        FocusText("Your rhythm", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        FocusText("A clear look at your recent progress.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(7 to "7 days", 30 to "30 days").forEach { (days, label) ->
                            FilterChip(selected = insightPeriodDays == days, onClick = { insightPeriodDays = days }, label = { FocusText(label) })
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InsightMetric("Focus time", "${insights.focusMinutes} min", Modifier.weight(1f))
                    InsightMetric("Sessions", insights.completedSessions.toString(), Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InsightMetric("Tasks completed", insights.completedTasks.toString(), Modifier.weight(1f))
                    InsightMetric("Avg. session", "${insights.averageSessionMinutes} min", Modifier.weight(1f))
                }
                FocusText("Rolling window ending now · completed sessions only for focus stats.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f))
                FocusText("Focus time sums saved minutes; average divides by completed sessions.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f))
                FocusText("Completed focus sessions", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                val peak = insights.sessionTrend.maxOrNull()?.coerceAtLeast(1) ?: 1
                Row(Modifier.fillMaxWidth().height(78.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Bottom) {
                    insights.sessionTrend.forEachIndexed { index, count ->
                        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                            FocusText(count.toString(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f))
                            Surface(
                                modifier = Modifier.fillMaxWidth().height((10 + (count.toFloat() / peak * 30).toInt()).dp),
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                                color = if (count > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.12f)
                            ) {}
                            FocusText((index + 1).toString(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f))
                        }
                    }
                }
                FocusText("Seven equal windows across the selected period · based on completed sessions.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f))
                if (insights.hasUntrackedLegacyCompletions) {
                    FocusText("Task completion history starts tracking from this update.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f))
                }
            }
        }
        Spacer(Modifier.height(18.dp))
        if (history.isNotEmpty()) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FocusText("RECENT SESSIONS", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary, letterSpacing = 1.sp)
                history.take(5).forEach { session ->
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(16.dp)) {
                        Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                FocusText(if (session.outcome == "COMPLETED") "Focus completed" else if (session.outcome == "RUNNING") "Focus in progress" else "Focus paused", fontWeight = FontWeight.SemiBold)
                                val taskTitle = session.taskId?.let { id -> tasks.firstOrNull { it.id == id }?.title }
                                FocusText(listOfNotNull(formatEpochDate(session.startedAtEpochMillis, LocalAppLanguage.current), taskTitle).joinToString(" · "), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            FocusText(formatTimer(session.accumulatedRunningMillis), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
    }
    if (confirmDiscard) AlertDialog(onDismissRequest = { confirmDiscard = false }, title = { FocusText("Discard this session?") }, text = { FocusText("This focus time will not be included in your history.") }, confirmButton = { TextButton(onClick = { onDiscard(); confirmDiscard = false }) { FocusText("Discard") } }, dismissButton = { TextButton(onClick = { confirmDiscard = false }) { FocusText("Keep going") } })
}

@Composable
private fun InsightMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            FocusText(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            FocusText(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun formatTimer(milliseconds: Long): String {
    val seconds = milliseconds.coerceAtLeast(0L) / 1_000L
    return "${(seconds / 60).toString().padStart(2, '0')}:${(seconds % 60).toString().padStart(2, '0')}"
}
