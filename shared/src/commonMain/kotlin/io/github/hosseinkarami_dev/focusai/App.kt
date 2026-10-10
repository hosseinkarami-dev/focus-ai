package io.github.hosseinkarami_dev.focusai

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text as MaterialText
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import io.github.hosseinkarami_dev.focusai.data.TaskEntity
import io.github.hosseinkarami_dev.focusai.data.NoteEntity
import io.github.hosseinkarami_dev.focusai.data.TaskStepDraft
import io.github.hosseinkarami_dev.focusai.data.TaskProposal
import io.github.hosseinkarami_dev.focusai.data.AppSettingsEntity
import io.github.hosseinkarami_dev.focusai.formatEpochDate
import io.github.hosseinkarami_dev.focusai.presentation.SettingsViewModel
import io.github.hosseinkarami_dev.focusai.presentation.TodayViewModel
import io.github.hosseinkarami_dev.focusai.presentation.FocusViewModel
import io.github.hosseinkarami_dev.focusai.presentation.elapsedMillis
import io.github.hosseinkarami_dev.focusai.ui.FocusAiMark
import io.github.hosseinkarami_dev.focusai.ui.LocalAppLanguage
import io.github.hosseinkarami_dev.focusai.ui.localizedText
import io.github.hosseinkarami_dev.focusai.ui.theme.FocusTheme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private enum class Destination(val label: String) { Today("Today"), Inbox("Inbox"), Tasks("Tasks"), Focus("Focus"), Settings("Settings") }
private data class ReviewDraft(val noteId: String, val proposals: List<TaskProposal>)

private fun isValidIsoDate(value: String): Boolean {
    val match = Regex("^(\\d{4})-(\\d{2})-(\\d{2})$").matchEntire(value) ?: return false
    val year = match.groupValues[1].toInt()
    val month = match.groupValues[2].toInt()
    val day = match.groupValues[3].toInt()
    if (year !in 1..9999 || month !in 1..12) return false
    val leapYear = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    val daysInMonth = when (month) {
        2 -> if (leapYear) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
    return day in 1..daysInMonth
}

@Composable
fun App() {
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val focusViewModel = koinViewModel<FocusViewModel>()
    val preferences by settingsViewModel.settings.collectAsState()
    FocusTheme(themeMode = preferences.themeMode) {
      CompositionLocalProvider(
        LocalAppLanguage provides preferences.language,
        LocalLayoutDirection provides if (preferences.language == "fa") LayoutDirection.Rtl else LayoutDirection.Ltr
      ) {
        val viewModel = koinViewModel<TodayViewModel>()
        val tasks by viewModel.taskList.collectAsState()
        val notes by viewModel.noteList.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        var destination by rememberSaveable { mutableStateOf(Destination.Today.name) }
        var showTaskEditor by rememberSaveable { mutableStateOf(false) }
        var taskToEdit by remember { mutableStateOf<TaskEntity?>(null) }
        var showCapture by rememberSaveable { mutableStateOf(false) }
        var noteToEdit by remember { mutableStateOf<NoteEntity?>(null) }
        var review by remember { mutableStateOf<ReviewDraft?>(null) }

        val onToggleTask: (TaskEntity) -> Unit = { task ->
            val nextCompleted = !task.completed
            viewModel.setCompleted(task, nextCompleted)
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = if (nextCompleted) "Task completed" else "Task marked open",
                    actionLabel = "Undo",
                    withDismissAction = true
                )
                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    viewModel.setCompleted(task, task.completed)
                }
            }
        }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = { SnackbarHost(snackbarHostState) { data ->
                Surface(color = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer, shape = RoundedCornerShape(18.dp), shadowElevation = 8.dp) {
                    Row(Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        FocusText(data.visuals.message, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                        data.visuals.actionLabel?.let { label -> TextButton(onClick = data::performAction) { FocusText(label, color = MaterialTheme.colorScheme.primary) } }
                    }
                }
            } },
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    Destination.entries.forEach { item ->
                        NavigationBarItem(
                            selected = destination == item.name,
                            onClick = { destination = item.name },
                            icon = { Icon(item.icon(), contentDescription = null) },
                            label = { FocusText(item.label) }
                        )
                    }
                }
            },
            floatingActionButton = {
                if (destination == Destination.Today.name || destination == Destination.Tasks.name || destination == Destination.Inbox.name) {
                    androidx.compose.material3.FloatingActionButton(
                        onClick = {
                            if (destination == Destination.Inbox.name) {
                                noteToEdit = null
                                showCapture = true
                            } else {
                                taskToEdit = null
                                showTaskEditor = true
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) { Icon(Icons.Filled.Add, contentDescription = localizedText(if (destination == Destination.Inbox.name) "Capture a thought" else "Add task", preferences.language)) }
                }
            }
        ) { padding ->
            when (Destination.valueOf(destination)) {
                Destination.Today -> TodayScreen(tasks, padding, onCapture = { noteToEdit = null; showCapture = true }, onFocus = { destination = Destination.Focus.name }, onToggle = onToggleTask, onEditTask = { taskToEdit = it; showTaskEditor = true })
                Destination.Tasks -> TaskListScreen(
                    tasks,
                    padding,
                    onToggle = onToggleTask,
                    onEditTask = { taskToEdit = it; showTaskEditor = true },
                    onMoveTask = { task, direction -> viewModel.moveTask(task, direction) },
                    onAddTask = { taskToEdit = null; showTaskEditor = true }
                )
                Destination.Inbox -> InboxScreen(
                    notes = notes,
                    padding = padding,
                    onCapture = { noteToEdit = null; showCapture = true },
                    onOpenNote = { note -> noteToEdit = note; showCapture = true },
                    onDeleteNote = { note -> viewModel.deleteNote(note.id) },
                    onArchiveNote = { note, archived -> viewModel.setNoteArchived(note.id, archived) }
                )
                Destination.Focus -> {
                    val activeSession by focusViewModel.active.collectAsState()
                    val history by focusViewModel.history.collectAsState()
                    FocusScreen(
                        tasks = tasks, padding = padding, focusLengthMinutes = preferences.focusLengthMinutes,
                        activeSession = activeSession, history = history,
                        onStart = focusViewModel::start, onPause = focusViewModel::pause,
                        onResume = focusViewModel::resume, onFinish = focusViewModel::finish,
                        onDiscard = focusViewModel::discard
                    )
                }
                Destination.Settings -> SettingsScreen(
                    padding = padding,
                    preferences = preferences,
                    onThemeMode = settingsViewModel::setThemeMode,
                    onFocusLength = settingsViewModel::setFocusLength,
                    onLanguage = settingsViewModel::setLanguage
                )
            }
        }

        if (showTaskEditor) TaskEditorDialog(
            task = taskToEdit,
            onDismiss = { showTaskEditor = false; taskToEdit = null },
            onSave = { title, details, priority, estimate, dueDate, subtasks ->
                viewModel.saveTask(title, details, priority, estimate, dueDate, subtasks, taskToEdit)
                showTaskEditor = false
                taskToEdit = null
            },
            onDelete = { task -> viewModel.deleteTask(task); showTaskEditor = false; taskToEdit = null }
        )
        if (showCapture) CaptureDialog(
            existingNote = noteToEdit,
            onDismiss = { showCapture = false },
            onSaved = { showCapture = false; noteToEdit = null },
            onReview = { noteId, proposals -> showCapture = false; noteToEdit = null; review = ReviewDraft(noteId, proposals) }
        )
        review?.let { draft ->
            ReviewDialog(draft.proposals, onDismiss = { review = null }, onConfirm = { selected ->
                viewModel.confirm(selected, draft.noteId)
                review = null
            })
        }
      }
    }
}

@Composable
private fun Destination.icon() = when (this) {
    Destination.Today -> Icons.Filled.Home
    Destination.Inbox -> Icons.Filled.Inbox
    Destination.Tasks -> Icons.Filled.TaskAlt
    Destination.Focus -> Icons.Filled.Timer
    Destination.Settings -> Icons.Filled.Settings
}

@Composable
private fun TodayScreen(tasks: List<TaskEntity>, padding: PaddingValues, onCapture: () -> Unit, onFocus: () -> Unit, onToggle: (TaskEntity) -> Unit, onEditTask: (TaskEntity) -> Unit) {
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
private fun EmptyPlan(onCapture: () -> Unit) {
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
private fun TaskRow(
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
private fun TaskListScreen(
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

@Composable
private fun InboxScreen(notes: List<NoteEntity>, padding: PaddingValues, onCapture: () -> Unit, onOpenNote: (NoteEntity) -> Unit, onDeleteNote: (NoteEntity) -> Unit, onArchiveNote: (NoteEntity, Boolean) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf("All") }
    var noteToDelete by remember { mutableStateOf<NoteEntity?>(null) }
    val visibleNotes = notes.filter { note ->
        val matchesQuery = note.body.contains(query, ignoreCase = true)
        val matchesFilter = when (filter) {
            "To review" -> !note.processed && !note.archived
            "Reviewed" -> note.processed && !note.archived
            "Archived" -> note.archived
            else -> true
        }
        matchesQuery && matchesFilter
    }
    Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 22.dp)) {
        Spacer(Modifier.height(22.dp))
        FocusText("Smart Inbox", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        FocusText("A safe place for everything on your mind.", color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { FocusText("Search notes") },
            leadingIcon = { Icon(Icons.Filled.Inbox, contentDescription = null) },
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "To review", "Reviewed", "Archived").forEach { option ->
                FilterChip(selected = filter == option, onClick = { filter = option }, label = { FocusText(option) })
            }
        }
        Spacer(Modifier.height(8.dp))
        if (notes.isEmpty()) {
            Column(Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(78.dp)) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.Inbox, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp)) }
                }
                Spacer(Modifier.height(17.dp))
                FocusText("Your inbox is clear", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                FocusText("Drop in a thought whenever it arrives.\nIt’ll be here when you need it.", color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.Center)
                Spacer(Modifier.height(14.dp))
                Button(onClick = onCapture, shape = RoundedCornerShape(14.dp)) { Icon(Icons.Filled.Add, null); Spacer(Modifier.width(8.dp)); FocusText("Capture a thought") }
            }
        } else if (visibleNotes.isEmpty()) {
            Column(Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                FocusText("No matching notes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                FocusText("Try a different search or filter.", color = MaterialTheme.colorScheme.secondary)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
                items(visibleNotes, key = { it.id }) { note ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onOpenNote(note) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                FocusText(note.body.lineSequence().firstOrNull().orEmpty().ifBlank { "Untitled note" }, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                IconButton(onClick = { onArchiveNote(note, !note.archived) }, modifier = Modifier.size(36.dp)) {
                                    Icon(
                                        imageVector = if (note.archived) Icons.Filled.Unarchive else Icons.Filled.Archive,
                                        contentDescription = if (note.archived) "Restore note" else "Archive note",
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                IconButton(onClick = { noteToDelete = note }, modifier = Modifier.size(36.dp)) {
                                    Icon(Icons.Filled.DeleteOutline, contentDescription = localizedText("Delete note", LocalAppLanguage.current), tint = MaterialTheme.colorScheme.secondary)
                                }
                            }
                            FocusText(note.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                FocusText(
                                    when {
                                        note.archived -> "Archived"
                                        note.processed -> "Reviewed"
                                        else -> "To review"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)
                                )
                                FocusText("Updated ${formatEpochDate(note.updatedAtEpochMillis, LocalAppLanguage.current)}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
        }
    }
    noteToDelete?.let { note ->
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = { FocusText("Delete this note?") },
            text = { FocusText("This note will be permanently removed from this device.") },
            confirmButton = {
                TextButton(onClick = { onDeleteNote(note); noteToDelete = null }) {
                    FocusText("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { noteToDelete = null }) { FocusText("Keep note") } },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun FocusScreen(
    tasks: List<TaskEntity>, padding: PaddingValues, focusLengthMinutes: Int,
    activeSession: io.github.hosseinkarami_dev.focusai.data.FocusSessionEntity?,
    history: List<io.github.hosseinkarami_dev.focusai.data.FocusSessionEntity>,
    onStart: (Int, String?) -> Unit, onPause: () -> Unit, onResume: () -> Unit,
    onFinish: () -> Unit, onDiscard: () -> Unit
) {
    var now by remember { mutableStateOf(currentTimeMillis()) }
    var selectedTaskId by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmDiscard by remember { mutableStateOf(false) }
    LaunchedEffect(activeSession?.id, activeSession?.lastResumedAtEpochMillis) {
        while (activeSession?.lastResumedAtEpochMillis != null) {
            now = currentTimeMillis()
            kotlinx.coroutines.delay(1_000)
        }
    }
    val elapsed = activeSession?.elapsedMillis(now) ?: 0L
    val planned = activeSession?.plannedDurationSeconds?.times(1_000L) ?: focusLengthMinutes * 60_000L
    val remaining = (planned - elapsed).coerceAtLeast(0L)
    val progress = if (planned > 0) (elapsed.toFloat() / planned).coerceIn(0f, 1f) else 0f
    Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        FocusText("Focus", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        FocusText("A little room to do your best work.", color = MaterialTheme.colorScheme.secondary)
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
        Spacer(Modifier.height(30.dp))
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

private fun formatTimer(milliseconds: Long): String {
    val seconds = milliseconds.coerceAtLeast(0L) / 1_000L
    return "${(seconds / 60).toString().padStart(2, '0')}:${(seconds % 60).toString().padStart(2, '0')}"
}

@Composable
private fun FocusText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = LocalTextStyle.current
) {
    MaterialText(
        text = localizedText(text, LocalAppLanguage.current), modifier = modifier, color = color,
        fontSize = fontSize, fontStyle = fontStyle, fontWeight = fontWeight, fontFamily = fontFamily,
        letterSpacing = letterSpacing, textDecoration = textDecoration, textAlign = textAlign,
        lineHeight = lineHeight, overflow = overflow, softWrap = softWrap, maxLines = maxLines,
        minLines = minLines, style = style
    )
}

@Composable
private fun SettingsScreen(
    padding: PaddingValues,
    preferences: AppSettingsEntity,
    onThemeMode: (String) -> Unit,
    onFocusLength: (Int) -> Unit,
    onLanguage: (String) -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            FocusAiMark(size = 54.dp)
            Column {
                FocusText("Make it yours", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                FocusText("A calmer setup, tuned to you.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            }
        }
        SettingsCard(title = "Appearance", subtitle = "Choose the look that feels right.") {
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                listOf("System", "Light", "Dark").forEach { option ->
                    FilterChip(
                        selected = preferences.themeMode == option,
                        onClick = { onThemeMode(option) },
                        label = { FocusText(option) },
                        leadingIcon = {
                            Icon(
                                imageVector = when (option) {
                                    "Light" -> Icons.Filled.LightMode
                                    "Dark" -> Icons.Filled.DarkMode
                                    else -> Icons.Filled.BrightnessAuto
                                },
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }
            FocusText(
                when (preferences.themeMode) {
                    "Light" -> "Soft lilac canvas and apricot accents."
                    "Dark" -> "Ink surfaces with luminous lilac accents."
                    else -> "Matches your device appearance."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        SettingsCard(title = "Language", subtitle = "Choose your app language.") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("en" to "English", "fa" to "فارسی", "es" to "Español").forEach { (code, label) ->
                    FilterChip(selected = preferences.language == code, onClick = { onLanguage(code) }, label = { FocusText(label) })
                }
            }
        }
        SettingsCard(title = "Focus session", subtitle = "Pick a comfortable starting length.") {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(15, 25, 45, 60).forEach { minutes ->
                    FilterChip(
                        selected = preferences.focusLengthMinutes == minutes,
                        onClick = { onFocusLength(minutes) },
                        label = { FocusText("$minutes min") }
                    )
                }
            }
            FocusText("New focus sessions will use this duration.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
        }
        SettingsCard(title = "Your privacy", subtitle = "Tasks and notes stay on this device.") {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(38.dp)) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.Shield, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                }
                FocusText("Nothing leaves your device during normal use.", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            }
        }
        SettingsCard(title = "AI suggestions", subtitle = "Sample mode · no service is contacted.") {
            FocusText("Suggestions are generated on-device for this preview. Your note is never uploaded.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
        }
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FocusAiMark(size = 42.dp)
                Column {
                    FocusText("FocusAI", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    FocusText("Productivity, with a little more clarity.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun SettingsCard(title: String, subtitle: String, content: @Composable () -> Unit) {
    Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FocusText(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            FocusText(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            content()
        }
    }
}

@Composable
private fun TaskEditorDialog(
    task: TaskEntity?,
    onDismiss: () -> Unit,
    onSave: suspend (String, String, String, Int, String?, List<TaskStepDraft>) -> Unit,
    onDelete: (TaskEntity) -> Unit
) {
    val vm = koinViewModel<TodayViewModel>()
    val scope = rememberCoroutineScope()
    var title by rememberSaveable(task?.id) { mutableStateOf(task?.title.orEmpty()) }
    var details by rememberSaveable(task?.id) { mutableStateOf(task?.details.orEmpty()) }
    var priority by rememberSaveable(task?.id) { mutableStateOf(task?.priority ?: "Medium") }
    var estimate by rememberSaveable(task?.id) { mutableStateOf((task?.estimateMinutes ?: 25).toString()) }
    var dueDate by rememberSaveable(task?.id) { mutableStateOf(task?.dueDateIso.orEmpty()) }
    val subtasks = remember(task?.id) { mutableStateListOf<TaskStepDraft>() }
    var loadingSubtasks by remember(task?.id) { mutableStateOf(task != null) }
    var saving by remember { mutableStateOf(false) }
    var saveError by remember { mutableStateOf<String?>(null) }
    var confirmDelete by remember { mutableStateOf(false) }
    val estimateValue = estimate.toIntOrNull()
    val validDueDate = dueDate.isBlank() || isValidIsoDate(dueDate)

    LaunchedEffect(task?.id) {
        if (task != null) {
            try {
                subtasks.addAll(vm.loadSubtasks(task.id).map { TaskStepDraft(it.id, it.title, it.completed) })
            } finally {
                loadingSubtasks = false
            }
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FocusText(if (task == null) "Add a task" else "Task details", modifier = Modifier.weight(1f))
                if (task != null) IconButton(onClick = { confirmDelete = true }) { Icon(Icons.Filled.DeleteOutline, contentDescription = localizedText("Delete task", LocalAppLanguage.current), tint = MaterialTheme.colorScheme.error) }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(value = title, onValueChange = { title = it }, modifier = Modifier.fillMaxWidth(), label = { FocusText("Task") }, singleLine = true, shape = RoundedCornerShape(14.dp))
                OutlinedTextField(value = details, onValueChange = { details = it }, modifier = Modifier.fillMaxWidth(), label = { FocusText("Details (optional)") }, minLines = 2, maxLines = 4, shape = RoundedCornerShape(14.dp))
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it; saveError = null },
                    modifier = Modifier.fillMaxWidth(),
                    label = { FocusText("Due date (optional)") },
                    placeholder = { FocusText("YYYY-MM-DD") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    supportingText = { FocusText(if (validDueDate) "Use year-month-day format" else "Enter a real calendar date, such as 2026-10-12") },
                    isError = !validDueDate
                )
                FocusText("PRIORITY", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold, letterSpacing = .8.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Low", "Medium", "High").forEach { option ->
                        FilterChip(selected = priority == option, onClick = { priority = option }, label = { FocusText(option) })
                    }
                }
                OutlinedTextField(value = estimate, onValueChange = { value -> if (value.all(Char::isDigit)) estimate = value }, modifier = Modifier.fillMaxWidth(), label = { FocusText("Time estimate in minutes") }, singleLine = true, shape = RoundedCornerShape(14.dp), supportingText = { if (estimateValue == null || estimateValue !in 5..1440) FocusText("Choose between 5 and 1,440 minutes") })
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FocusText("Steps", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    TextButton(onClick = { subtasks.add(TaskStepDraft(null, "", false)) }) { Icon(Icons.Filled.Add, null); Spacer(Modifier.width(4.dp)); FocusText("Add step") }
                }
                if (loadingSubtasks) {
                    CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                } else if (subtasks.isEmpty()) {
                    FocusText("Break this task into smaller steps when helpful.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                }
                subtasks.forEachIndexed { index, step ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = step.completed, onCheckedChange = { checked -> subtasks[index] = step.copy(completed = checked) })
                        OutlinedTextField(
                            value = step.title,
                            onValueChange = { value -> subtasks[index] = step.copy(title = value) },
                            modifier = Modifier.weight(1f),
                            placeholder = { FocusText("Step ${index + 1}") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        IconButton(onClick = { subtasks.removeAt(index) }) { Icon(Icons.Filled.DeleteOutline, contentDescription = localizedText("Remove step ${index + 1}", LocalAppLanguage.current), tint = MaterialTheme.colorScheme.secondary) }
                    }
                }
                saveError?.let { FocusText(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    saving = true
                    scope.launch {
                        try {
                            onSave(title, details, priority, estimateValue!!, dueDate.takeIf { it.isNotBlank() }, subtasks.toList())
                        } catch (cancelled: CancellationException) {
                            throw cancelled
                        } catch (_: Throwable) {
                            saving = false
                            saveError = "Couldn’t save this task. Please try again."
                        }
                    }
                },
                enabled = title.isNotBlank() && estimateValue != null && estimateValue in 5..1440 && validDueDate && !loadingSubtasks && !saving
            ) { if (saving) CircularProgressIndicator(Modifier.size(17.dp), strokeWidth = 2.dp) else FocusText(if (task == null) "Add task" else "Save changes") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { FocusText("Cancel") } },
        shape = RoundedCornerShape(24.dp)
    )
    if (confirmDelete && task != null) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { FocusText("Delete this task?") },
            text = { FocusText("“${task.title}” will be permanently removed.") },
            confirmButton = { TextButton(onClick = { confirmDelete = false; onDelete(task) }) { FocusText("Delete", color = MaterialTheme.colorScheme.error) } },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { FocusText("Keep task") } },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun CaptureDialog(
    existingNote: NoteEntity?,
    onDismiss: () -> Unit,
    onSaved: () -> Unit,
    onReview: (String, List<TaskProposal>) -> Unit
) {
    val vm = koinViewModel<TodayViewModel>()
    val scope = rememberCoroutineScope()
    var note by rememberSaveable(existingNote?.id) { mutableStateOf(existingNote?.body.orEmpty()) }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { FocusText(if (existingNote == null) "Capture a thought" else "Edit note") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FocusText("Write it as it comes. Your note is saved here before any suggestions are reviewed.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                OutlinedTextField(value = note, onValueChange = { note = it; message = null }, modifier = Modifier.fillMaxWidth(), minLines = 4, maxLines = 7, placeholder = { FocusText("I need to…") })
                FocusText("Sample AI preview · no service is contacted", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                message?.let { FocusText(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            }
        },
        confirmButton = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(enabled = note.isNotBlank() && !loading, onClick = {
                    loading = true
                    scope.launch {
                        try {
                            vm.saveNote(note, existingNote)
                            onSaved()
                        } catch (cancelled: CancellationException) {
                            throw cancelled
                        } catch (_: Throwable) {
                            message = "Couldn’t save this note. Please try again."
                        } finally {
                            loading = false
                        }
                    }
                }) { FocusText("Save note") }
                Button(enabled = note.isNotBlank() && !loading, onClick = {
                    loading = true
                    scope.launch {
                        try {
                            val saved = vm.saveNote(note, existingNote)
                            val ideas = vm.analyze(note)
                            if (ideas.isEmpty()) message = "Add a little more detail and try again."
                            else onReview(saved.id, ideas)
                        } catch (cancelled: CancellationException) {
                            throw cancelled
                        } catch (_: Throwable) {
                            message = "Couldn’t create suggestions. Your note is saved; try again."
                        } finally {
                            loading = false
                        }
                    }
                }) {
                    if (loading) CircularProgressIndicator(Modifier.size(17.dp), strokeWidth = 2.dp) else Icon(Icons.Filled.AutoAwesome, null, modifier = Modifier.size(17.dp))
                    Spacer(Modifier.width(7.dp)); FocusText(if (loading) "Reading…" else "Suggest tasks")
                }
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { FocusText("Cancel") } },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun ReviewDialog(proposals: List<TaskProposal>, onDismiss: () -> Unit, onConfirm: (List<TaskProposal>) -> Unit) {
    val selected = remember(proposals) { mutableStateListOf<Int>().apply { addAll(proposals.indices) } }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                FocusText("Review task ideas")
                FocusText("SAMPLE PREVIEW · NOTHING SAVED YET", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, letterSpacing = .5.sp)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FocusText("Choose what you’d like to keep. You can edit each task later.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                proposals.forEachIndexed { index, item ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = index in selected, onCheckedChange = { checked -> if (checked) selected.add(index) else selected.remove(index) })
                        Column(Modifier.weight(1f)) {
                            FocusText(item.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            FocusText("${item.priority}  ·  ${item.estimateMinutes} min", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { onConfirm(selected.sorted().map { proposals[it] }) }, enabled = selected.isNotEmpty()) { FocusText("Add ${selected.size} tasks") } },
        dismissButton = { TextButton(onClick = onDismiss) { FocusText("Keep editing") } },
        shape = RoundedCornerShape(24.dp)
    )
}

