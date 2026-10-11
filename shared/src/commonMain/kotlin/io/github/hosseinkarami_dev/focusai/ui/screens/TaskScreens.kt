package io.github.hosseinkarami_dev.focusai.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import io.github.hosseinkarami_dev.focusai.ui.FocusText
import io.github.hosseinkarami_dev.focusai.ui.FocusAiMark
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import io.github.hosseinkarami_dev.focusai.currentLocalDateIso
import io.github.hosseinkarami_dev.focusai.epochMillisToIsoDate
import io.github.hosseinkarami_dev.focusai.formatEpochDate
import io.github.hosseinkarami_dev.focusai.formatWeekdayDate
import io.github.hosseinkarami_dev.focusai.isoDateToEpochMillis
import io.github.hosseinkarami_dev.focusai.shiftIsoDate
import io.github.hosseinkarami_dev.focusai.weekDatesFor
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
    val language = LocalAppLanguage.current
    Card(
        onClick = { onEdit(task) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onToggle(task) },
                modifier = Modifier.semantics {
                    contentDescription = localizedText(
                        if (task.completed) "Reopen task" else "Complete task",
                        language
                    ) + ": ${task.title}"
                }
            )
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f).padding(vertical = 4.dp)) {
                FocusText(task.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None, color = if (task.completed) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface)
                if (task.details.isNotBlank()) {
                    Spacer(Modifier.height(3.dp))
                    FocusText(task.details, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Spacer(Modifier.height(5.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FocusText(task.priority, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Icon(Icons.Filled.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(13.dp))
                        FocusText("${task.estimateMinutes} min", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    }
                    task.dueDateIso?.let {
                        FocusText("Due $it", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    }
                    task.recurrenceRule?.let {
                        FocusText(localizedText(it.lowercase().replaceFirstChar(Char::uppercase), language), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    }
                    task.reminderTimeLocal?.let {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(Icons.Filled.NotificationsNone, contentDescription = localizedText("Reminder", language), tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(13.dp))
                            FocusText(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
            if (onMove != null && !task.completed && (canMoveUp || canMoveDown)) {
                Column {
                    IconButton(onClick = { onMove(-1) }, enabled = canMoveUp) {
                        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = localizedText("Move task up", language) + ": ${task.title}", tint = MaterialTheme.colorScheme.secondary)
                    }
                    IconButton(onClick = { onMove(1) }, enabled = canMoveDown) {
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = localizedText("Move task down", language) + ": ${task.title}", tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
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
    onAddTask: (String?) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf("All") }
    var plannerMode by rememberSaveable { mutableStateOf(false) }
    var sortMode by rememberSaveable { mutableStateOf("My order") }
    var showSortMenu by remember { mutableStateOf(false) }
    var selectedDate by rememberSaveable { mutableStateOf(currentLocalDateIso()) }
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
    val sortOptions = listOf("My order", "Due date", "Priority", "Title")
    fun sorted(items: List<TaskEntity>): List<TaskEntity> = when (sortMode) {
        "Due date" -> items.sortedWith(compareBy<TaskEntity> { it.dueDateIso == null }.thenBy { it.dueDateIso }.thenBy { it.planOrder })
        "Priority" -> items.sortedWith(compareBy<TaskEntity> { when (it.priority) { "High" -> 0; "Medium" -> 1; else -> 2 } }.thenBy { it.planOrder })
        "Title" -> items.sortedWith(compareBy<TaskEntity> { it.title.lowercase() }.thenBy { it.planOrder })
        else -> items.sortedWith(compareBy<TaskEntity> { it.completed }.thenBy { it.planOrder }.thenBy { it.createdAtEpochMillis })
    }
    val sortedFiltered = sorted(filtered)
    val showPlanControls = sortMode == "My order" && query.isBlank() && filter != "Completed"
    Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 22.dp)) {
        Spacer(Modifier.height(22.dp))
        FocusText("Tasks", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        FocusText("A clear view of what’s on your plate.", color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(18.dp))
        FocusText("${tasks.count { !it.completed }} open  ·  ${tasks.count { it.completed }} completed", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(12.dp))
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.62f),
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(Modifier.padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(false, true).forEach { week ->
                    val selected = plannerMode == week
                    Surface(
                        onClick = { plannerMode = week },
                        shape = RoundedCornerShape(14.dp),
                        color = if (selected) MaterialTheme.colorScheme.surface else androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        shadowElevation = if (selected) 1.dp else 0.dp
                    ) {
                        Row(
                            Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(if (week) Icons.Filled.CalendarViewWeek else Icons.AutoMirrored.Filled.ViewList, contentDescription = null, modifier = Modifier.size(18.dp))
                            FocusText(localizedText(if (week) "Week" else "List", LocalAppLanguage.current), style = MaterialTheme.typography.labelLarge, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = { FocusText(localizedText("Search tasks", LocalAppLanguage.current)) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                shape = RoundedCornerShape(18.dp)
            )
            Box {
                Surface(
                    onClick = { showSortMenu = true },
                    shape = RoundedCornerShape(16.dp),
                    color = if (sortMode != "My order") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (sortMode != "My order") MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    Row(Modifier.padding(horizontal = 13.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = localizedText("Sort tasks", LocalAppLanguage.current), modifier = Modifier.size(20.dp))
                        FocusText(localizedText("Sort", LocalAppLanguage.current), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                    }
                }
                DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { FocusText(localizedText(option, LocalAppLanguage.current)) },
                            onClick = { sortMode = option; showSortMenu = false },
                            leadingIcon = { if (sortMode == option) Icon(Icons.Filled.Check, contentDescription = null) }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Open", "Completed").forEach { option ->
                FilterChip(selected = filter == option, onClick = { filter = option }, label = { FocusText(option) })
            }
        }
        if (plannerMode) {
            val weekDates = weekDatesFor(selectedDate)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = { selectedDate = shiftIsoDate(selectedDate, -7) ?: selectedDate }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = localizedText("Previous week", LocalAppLanguage.current))
                }
                FocusText(
                    if (weekDates.isEmpty()) "This week" else "${isoDateToEpochMillis(weekDates.first())?.let { formatEpochDate(it, LocalAppLanguage.current) }.orEmpty()} – ${isoDateToEpochMillis(weekDates.last())?.let { formatEpochDate(it, LocalAppLanguage.current) }.orEmpty()}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = { selectedDate = shiftIsoDate(selectedDate, 7) ?: selectedDate }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = localizedText("Next week", LocalAppLanguage.current))
                }
            }
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                weekDates.forEach { date ->
                    val count = tasks.count { it.dueDateIso == date && !it.completed }
                    Surface(
                        onClick = { selectedDate = date },
                        shape = RoundedCornerShape(18.dp),
                        color = if (selectedDate == date) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (selectedDate == date) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        Column(Modifier.width(64.dp).heightIn(min = 72.dp).padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                            FocusText(isoDateToEpochMillis(date)?.let { formatWeekdayDate(it, LocalAppLanguage.current) }.orEmpty(), style = MaterialTheme.typography.labelSmall)
                            FocusText(date.substringAfterLast('-').toIntOrNull()?.toString().orEmpty(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            FocusText(if (count == 0) " " else count.toString(), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                FocusText(isoDateToEpochMillis(selectedDate)?.let { formatEpochDate(it, LocalAppLanguage.current) }.orEmpty(), modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                TextButton(onClick = { onAddTask(selectedDate) }) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    FocusText(localizedText("Add task", LocalAppLanguage.current))
                }
            }
            val dayTasks = sortedFiltered.filter { it.dueDateIso == selectedDate }
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                if (dayTasks.isEmpty()) item {
                    Column(Modifier.fillMaxWidth().padding(vertical = 18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        FocusText(localizedText("No tasks for this day", LocalAppLanguage.current), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.secondary)
                    }
                }
                items(dayTasks, key = { it.id }) { task -> TaskRow(task, onToggle, onEditTask) }
            }
            return@Column
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
                Button(onClick = { onAddTask(null) }, shape = RoundedCornerShape(14.dp)) { Icon(Icons.Filled.Add, null); Spacer(Modifier.width(8.dp)); FocusText("Add a task") }
            }
            filtered.isEmpty() -> Column(Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                FocusText("No matching tasks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                FocusText("Try another search or filter.", color = MaterialTheme.colorScheme.secondary)
            }
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                items(sortedFiltered, key = { it.id }) { task ->
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
