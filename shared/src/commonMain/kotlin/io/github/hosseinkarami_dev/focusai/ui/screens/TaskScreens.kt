package io.github.hosseinkarami_dev.focusai.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import io.github.hosseinkarami_dev.focusai.ui.FocusText
import io.github.hosseinkarami_dev.focusai.ui.FocusAiMark
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import io.github.hosseinkarami_dev.focusai.data.TaskEntity
import io.github.hosseinkarami_dev.focusai.ui.FocusAiMark
import io.github.hosseinkarami_dev.focusai.ui.LocalAppLanguage
import io.github.hosseinkarami_dev.focusai.ui.localizedText

@Composable
internal fun TodayScreen(tasks: List<TaskEntity>, padding: PaddingValues, onCapture: () -> Unit, onFocus: () -> Unit, onToggle: (TaskEntity) -> Unit, onEditTask: (TaskEntity) -> Unit) {
    val active = tasks.filterNot { it.completed }
    val plannedMinutes = active.sumOf { it.estimateMinutes }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 18.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    FocusText("YOUR SPACE TO FOCUS", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold, letterSpacing = 1.1.sp)
                    Spacer(Modifier.height(5.dp))
                    FocusText("Make today count.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
                FocusAiMark(size = 48.dp)
            }
        }
        item {
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Column(Modifier.weight(1f)) {
                            FocusText("YOUR DAILY FOCUS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .72f), fontWeight = FontWeight.Bold, letterSpacing = 1.1.sp)
                            Spacer(Modifier.height(8.dp))
                            FocusText(if (active.isEmpty()) "A little clarity\ngoes a long way." else "One thing at\na time.", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, lineHeight = 30.sp)
                        }
                        Box(Modifier.size(62.dp).clip(CircleShape).background(MaterialTheme.colorScheme.onPrimary.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(27.dp))
                        }
                    }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = .8f), modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(7.dp))
                        FocusText(if (active.isEmpty()) "No tasks planned yet" else "$plannedMinutes min planned", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .9f), style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.weight(1f))
                        FocusText("${tasks.count { it.completed }} done", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .8f), style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onCapture),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .65f))
            ) {
                Row(Modifier.padding(horizontal = 16.dp, vertical = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(42.dp)) {
                        Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                    }
                    Spacer(Modifier.width(13.dp))
                    Column(Modifier.weight(1f)) {
                        FocusText("Turn a thought into a plan", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        FocusText("Capture a note and review task ideas", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                    Icon(Icons.Filled.ChevronRight, contentDescription = localizedText("Capture a note", LocalAppLanguage.current), tint = MaterialTheme.colorScheme.secondary)
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                FocusText("Your priorities", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                FocusText("${active.size} left", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            }
        }
        if (tasks.isEmpty()) {
            item { EmptyPlan(onCapture) }
        } else {
            items(tasks, key = { it.id }) { task -> TaskRow(task, onToggle, onEditTask) }
        }
        item {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Row(Modifier.fillMaxWidth().clickable(onClick = onFocus).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface, modifier = Modifier.size(42.dp)) {
                        Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        FocusText("A moment of focus", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        FocusText("Make space for one thing", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                    Icon(Icons.Filled.PlayArrow, contentDescription = localizedText("Start a focus session", LocalAppLanguage.current), tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
internal fun EmptyPlan(onCapture: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.size(60.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(29.dp)) }
        }
        Spacer(Modifier.height(12.dp))
        FocusText("Your day is open", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        FocusText("Add a task or start with a note.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onCapture) { FocusText("Capture a thought") }
    }
}

@Composable
internal fun TaskRow(
    task: TaskEntity,
    onToggle: (TaskEntity) -> Unit,
    onEdit: (TaskEntity) -> Unit,
    canMoveUp: Boolean = false,
    canMoveDown: Boolean = false,
    onMove: ((Int) -> Unit)? = null
) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = task.completed, onCheckedChange = { onToggle(task) })
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f).clickable { onEdit(task) }.padding(vertical = 4.dp)) {
                FocusText(task.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None, color = if (task.completed) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface)
                if (task.details.isNotBlank()) {
                    Spacer(Modifier.height(3.dp))
                    FocusText(task.details, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Spacer(Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FocusText(task.priority, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(9.dp))
                    Icon(Icons.Filled.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(3.dp))
                    FocusText("${task.estimateMinutes} min", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    task.dueDateIso?.let {
                        Spacer(Modifier.width(10.dp))
                        FocusText("Due $it", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
            if (onMove != null && !task.completed && (canMoveUp || canMoveDown)) {
                Column {
                    IconButton(onClick = { onMove(-1) }, enabled = canMoveUp, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = localizedText("Move ${task.title} up", LocalAppLanguage.current), tint = MaterialTheme.colorScheme.secondary)
                    }
                    IconButton(onClick = { onMove(1) }, enabled = canMoveDown, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = localizedText("Move ${task.title} down", LocalAppLanguage.current), tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
            IconButton(onClick = { onEdit(task) }) { Icon(Icons.Filled.ChevronRight, contentDescription = localizedText("Edit ${task.title}", LocalAppLanguage.current), tint = MaterialTheme.colorScheme.secondary) }
        }
    }
}

@Composable
internal fun TaskListScreen(
    tasks: List<TaskEntity>,
    padding: PaddingValues,
    onToggle: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit,
    onMoveTask: (TaskEntity, Int) -> Unit,
    onAddTask: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf("All") }
    val filtered = tasks.filter { task ->
        val matchesQuery = task.title.contains(query, ignoreCase = true) || task.details.contains(query, ignoreCase = true)
        val matchesFilter = when (filter) {
            "Open" -> !task.completed
            "Completed" -> task.completed
            else -> true
        }
        matchesQuery && matchesFilter
    }
    val openTasks = tasks.filterNot { it.completed }
    val showPlanControls = query.isBlank() && filter != "Completed"
    Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 22.dp)) {
        Spacer(Modifier.height(22.dp))
        FocusText("Tasks", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        FocusText("A clear view of what’s on your plate.", color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(18.dp))
        FocusText("${tasks.count { !it.completed }} open  ·  ${tasks.count { it.completed }} completed", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { FocusText("Search tasks") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Open", "Completed").forEach { option ->
                FilterChip(selected = filter == option, onClick = { filter = option }, label = { FocusText(option) })
            }
        }
        Spacer(Modifier.height(8.dp))
        when {
            tasks.isEmpty() -> Column(Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(70.dp)) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.TaskAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp)) }
                }
                Spacer(Modifier.height(16.dp))
                FocusText("A clear slate", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                FocusText("Add a task when something needs your attention.", color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.Center)
                Spacer(Modifier.height(14.dp))
                Button(onClick = onAddTask, shape = RoundedCornerShape(14.dp)) { Icon(Icons.Filled.Add, null); Spacer(Modifier.width(8.dp)); FocusText("Add a task") }
            }
            filtered.isEmpty() -> Column(Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                FocusText("No matching tasks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                FocusText("Try another search or filter.", color = MaterialTheme.colorScheme.secondary)
            }
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                items(filtered, key = { it.id }) { task ->
                    val position = openTasks.indexOfFirst { it.id == task.id }
                    TaskRow(
                        task = task,
                        onToggle = onToggle,
                        onEdit = onEditTask,
                        canMoveUp = showPlanControls && position > 0,
                        canMoveDown = showPlanControls && position >= 0 && position < openTasks.lastIndex,
                        onMove = if (showPlanControls && position >= 0) ({ direction -> onMoveTask(task, direction) }) else null
                    )
                }
            }
        }
    }
}
