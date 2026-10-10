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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hosseinkarami_dev.focusai.data.TaskEntity
import io.github.hosseinkarami_dev.focusai.data.TaskProposal
import io.github.hosseinkarami_dev.focusai.di.initKoin
import io.github.hosseinkarami_dev.focusai.presentation.TodayViewModel
import io.github.hosseinkarami_dev.focusai.ui.theme.FocusTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private enum class Destination(val label: String) { Today("Today"), Inbox("Inbox"), Tasks("Tasks"), Focus("Focus"), Settings("Settings") }

@Composable
fun App() {
    FocusTheme {
        val viewModel = koinViewModel<TodayViewModel>()
        val tasks by viewModel.taskList.collectAsState()
        var destination by rememberSaveable { mutableStateOf(Destination.Today.name) }
        var showAddTask by rememberSaveable { mutableStateOf(false) }
        var showCapture by rememberSaveable { mutableStateOf(false) }
        var review by remember { mutableStateOf<List<TaskProposal>?>(null) }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    Destination.entries.forEach { item ->
                        NavigationBarItem(
                            selected = destination == item.name,
                            onClick = { destination = item.name },
                            icon = { Icon(item.icon(), contentDescription = null) },
                            label = { Text(item.label) }
                        )
                    }
                }
            },
            floatingActionButton = {
                if (destination == Destination.Today.name || destination == Destination.Tasks.name) {
                    androidx.compose.material3.FloatingActionButton(
                        onClick = { showAddTask = true },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) { Icon(Icons.Filled.Add, contentDescription = "Add task") }
                }
            }
        ) { padding ->
            when (Destination.valueOf(destination)) {
                Destination.Today -> TodayScreen(tasks, padding, onCapture = { showCapture = true }, onFocus = { destination = Destination.Focus.name }, onToggle = viewModel::toggle)
                Destination.Tasks -> TaskListScreen(tasks, padding, onToggle = viewModel::toggle)
                Destination.Inbox -> InboxScreen(padding, onCapture = { showCapture = true })
                Destination.Focus -> FocusScreen(tasks.count { !it.completed }, padding)
                Destination.Settings -> SettingsScreen(padding)
            }
        }

        if (showAddTask) AddTaskDialog(onDismiss = { showAddTask = false }, onAdd = { title -> viewModel.addTask(title); showAddTask = false })
        if (showCapture) CaptureDialog(
            onDismiss = { showCapture = false },
            onReview = { proposals -> showCapture = false; review = proposals }
        )
        review?.let { proposals ->
            ReviewDialog(proposals, onDismiss = { review = null }, onConfirm = { selected ->
                viewModel.confirm(selected)
                review = null
            })
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
private fun TodayScreen(tasks: List<TaskEntity>, padding: PaddingValues, onCapture: () -> Unit, onFocus: () -> Unit, onToggle: (TaskEntity) -> Unit) {
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
                    Text("MONDAY, OCTOBER 10", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold, letterSpacing = 1.1.sp)
                    Spacer(Modifier.height(5.dp))
                    Text("Make today count.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(44.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("F", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Column(Modifier.weight(1f)) {
                            Text("YOUR DAILY FOCUS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .72f), fontWeight = FontWeight.Bold, letterSpacing = 1.1.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(if (active.isEmpty()) "A little clarity\ngoes a long way." else "One thing at\na time.", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, lineHeight = 30.sp)
                        }
                        Box(Modifier.size(62.dp).clip(CircleShape).background(MaterialTheme.colorScheme.onPrimary.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(27.dp))
                        }
                    }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = .8f), modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(7.dp))
                        Text(if (active.isEmpty()) "No tasks planned yet" else "$plannedMinutes min planned", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .9f), style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.weight(1f))
                        Text("${tasks.count { it.completed }} done", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .8f), style = MaterialTheme.typography.labelMedium)
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
                        Text("Turn a thought into a plan", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text("Capture a note and review task ideas", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                    Icon(Icons.Filled.ChevronRight, contentDescription = "Capture a note", tint = MaterialTheme.colorScheme.secondary)
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Your priorities", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("${active.size} left", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            }
        }
        if (tasks.isEmpty()) {
            item { EmptyPlan(onCapture) }
        } else {
            items(tasks, key = { it.id }) { task -> TaskRow(task, onToggle) }
        }
        item {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Row(Modifier.fillMaxWidth().clickable(onClick = onFocus).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface, modifier = Modifier.size(42.dp)) {
                        Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("A moment of focus", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text("Make space for one thing", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Start a focus session", tint = MaterialTheme.colorScheme.primary)
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
        Text("Your day is open", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text("Add a task or start with a note.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onCapture) { Text("Capture a thought") }
    }
}

@Composable
private fun TaskRow(task: TaskEntity, onToggle: (TaskEntity) -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Row(Modifier.fillMaxWidth().clickable { onToggle(task) }.padding(horizontal = 14.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = task.completed, onCheckedChange = { onToggle(task) })
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None, color = if (task.completed) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(task.priority, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(9.dp))
                    Icon(Icons.Filled.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(3.dp))
                    Text("${task.estimateMinutes} min", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
private fun TaskListScreen(tasks: List<TaskEntity>, padding: PaddingValues, onToggle: (TaskEntity) -> Unit) {
    Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 22.dp)) {
        Spacer(Modifier.height(22.dp))
        Text("Tasks", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("A clear view of what’s on your plate.", color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(18.dp))
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
            Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.width(9.dp))
                Text("Your tasks stay on this device", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            }
        }
        Spacer(Modifier.height(18.dp))
        if (tasks.isEmpty()) EmptyPlan {} else LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
            items(tasks, key = { it.id }) { TaskRow(it, onToggle) }
        }
    }
}

@Composable
private fun InboxScreen(padding: PaddingValues, onCapture: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(padding).padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Smart Inbox", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("A safe place for everything on your mind.", color = MaterialTheme.colorScheme.secondary)
            }
            Icon(Icons.Filled.Search, contentDescription = "Search notes", tint = MaterialTheme.colorScheme.secondary)
        }
        Spacer(Modifier.weight(1f))
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(86.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.Inbox, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(39.dp)) }
        }
        Spacer(Modifier.height(18.dp))
        Text("Your inbox is clear", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text("Drop in a thought whenever it arrives.\nIt’ll be here when you need it.", color = MaterialTheme.colorScheme.secondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onCapture, shape = RoundedCornerShape(14.dp)) { Icon(Icons.Filled.Add, null); Spacer(Modifier.width(8.dp)); Text("Capture a thought") }
        Spacer(Modifier.weight(1.2f))
    }
}

@Composable
private fun FocusScreen(openTasks: Int, padding: PaddingValues) {
    Column(Modifier.fillMaxSize().padding(padding).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        Text("Focus", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("A little room to do your best work.", color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.weight(1f))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(250.dp)) {
            CircularProgressIndicator(progress = { 1f }, modifier = Modifier.fillMaxSize(), strokeWidth = 8.dp, color = MaterialTheme.colorScheme.primaryContainer, trackColor = MaterialTheme.colorScheme.primaryContainer)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("25:00", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Medium)
                Text("MINDFUL MINUTES", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, letterSpacing = 1.1.sp)
            }
        }
        Spacer(Modifier.height(26.dp))
        Text(if (openTasks == 0) "Take a breath, then begin." else "$openTasks tasks ready when you are.", color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(20.dp))
        Button(onClick = {}, shape = RoundedCornerShape(18.dp), contentPadding = PaddingValues(horizontal = 28.dp, vertical = 15.dp)) {
            Icon(Icons.Filled.PlayArrow, null); Spacer(Modifier.width(8.dp)); Text("Start focus", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.weight(1f))
        Text("Sessions will appear here as you focus", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(18.dp))
    }
}

@Composable
private fun SettingsScreen(padding: PaddingValues) {
    Column(Modifier.fillMaxSize().padding(padding).padding(22.dp)) {
        Spacer(Modifier.height(22.dp))
        Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Make FocusAI feel like yours.", color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(24.dp))
        Text("PREFERENCES", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(Modifier.height(9.dp))
        SettingRow("Appearance", "Follows your device theme")
        SettingRow("Focus length", "25 minutes")
        Spacer(Modifier.height(22.dp))
        Text("YOUR DATA", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(Modifier.height(9.dp))
        SettingRow("Privacy", "Tasks and notes stay on this device")
        SettingRow("AI suggestions", "Sample preview · no note is sent")
        Spacer(Modifier.weight(1f))
        Text("FOCUSAI  ·  PRODUCTIVITY, WITH A LITTLE MORE CLARITY", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
private fun SettingRow(title: String, summary: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) { Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold); Text(summary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary) }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
private fun AddTaskDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var title by rememberSaveable { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add a task") },
        text = { OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("What needs doing?") }, singleLine = true) },
        confirmButton = { TextButton(onClick = { onAdd(title) }, enabled = title.isNotBlank()) { Text("Add task") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        shape = RoundedCornerShape(24.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CaptureDialog(onDismiss: () -> Unit, onReview: (List<TaskProposal>) -> Unit) {
    val vm = koinViewModel<TodayViewModel>()
    val scope = rememberCoroutineScope()
    var note by rememberSaveable { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Capture a thought") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Write it as it comes. You’ll review every task idea before anything is saved.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                OutlinedTextField(value = note, onValueChange = { note = it; message = null }, modifier = Modifier.fillMaxWidth(), minLines = 4, maxLines = 7, placeholder = { Text("I need to…") })
                Text("Sample AI preview · your note stays on this device", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                message?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            }
        },
        confirmButton = {
            Button(enabled = note.isNotBlank() && !loading, onClick = {
                loading = true
                scope.launch {
                    val ideas = vm.analyze(note)
                    loading = false
                    if (ideas.isEmpty()) message = "Add a little more detail and try again."
                    else onReview(ideas)
                }
            }) {
                if (loading) CircularProgressIndicator(Modifier.size(17.dp), strokeWidth = 2.dp) else Icon(Icons.Filled.AutoAwesome, null, modifier = Modifier.size(17.dp))
                Spacer(Modifier.width(7.dp)); Text(if (loading) "Reading…" else "Suggest tasks")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Save for later") } },
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
                Text("Review task ideas")
                Text("SAMPLE PREVIEW · NOTHING SAVED YET", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, letterSpacing = .5.sp)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Choose what you’d like to keep. You can edit each task later.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                proposals.forEachIndexed { index, item ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = index in selected, onCheckedChange = { checked -> if (checked) selected.add(index) else selected.remove(index) })
                        Column(Modifier.weight(1f)) {
                            Text(item.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            Text("${item.priority}  ·  ${item.estimateMinutes} min", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { onConfirm(selected.sorted().map { proposals[it] }) }, enabled = selected.isNotEmpty()) { Text("Add ${selected.size} tasks") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Keep editing") } },
        shape = RoundedCornerShape(24.dp)
    )
}

