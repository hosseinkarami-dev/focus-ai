package io.github.hosseinkarami_dev.focusai.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import io.github.hosseinkarami_dev.focusai.ui.FocusText
import io.github.hosseinkarami_dev.focusai.ui.SectionCard
import io.github.hosseinkarami_dev.focusai.formatEpochDate
import io.github.hosseinkarami_dev.focusai.isoDateToEpochMillis
import io.github.hosseinkarami_dev.focusai.epochMillisToIsoDate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hosseinkarami_dev.focusai.data.TaskEntity
import io.github.hosseinkarami_dev.focusai.data.TaskStepDraft
import io.github.hosseinkarami_dev.focusai.presentation.TodayViewModel
import io.github.hosseinkarami_dev.focusai.ui.LocalAppLanguage
import io.github.hosseinkarami_dev.focusai.ui.localizedText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
internal fun TaskEditorScreen(
    task: TaskEntity?,
    padding: PaddingValues,
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
    var showDatePicker by remember { mutableStateOf(false) }
    val estimateValue = estimate.toIntOrNull()

    LaunchedEffect(task?.id) {
        if (task != null) {
            try {
                subtasks.addAll(vm.loadSubtasks(task.id).map { TaskStepDraft(it.id, it.title, it.completed) })
            } finally {
                loadingSubtasks = false
            }
        }
    }
    Column(Modifier.fillMaxSize().padding(padding).padding(top = 64.dp).imePadding()) {
      Column(
          Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 22.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(54.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.TaskAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(26.dp)) }
            }
            Column(Modifier.weight(1f)) {
                FocusText(if (task == null) "A fresh start" else "Refine your task", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                FocusText("Give this task a little shape.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            if (task != null) IconButton(onClick = { confirmDelete = true }) { Icon(Icons.Filled.DeleteOutline, contentDescription = localizedText("Delete task", LocalAppLanguage.current), tint = MaterialTheme.colorScheme.error) }
        }
        SectionCard(title = "Task details", subtitle = "Start with the outcome you want.") {
            OutlinedTextField(value = title, onValueChange = { title = it; saveError = null }, modifier = Modifier.fillMaxWidth(), label = { FocusText("Task") }, singleLine = true, shape = RoundedCornerShape(16.dp))
            OutlinedTextField(value = details, onValueChange = { details = it }, modifier = Modifier.fillMaxWidth(), label = { FocusText("Details (optional)") }, minLines = 3, maxLines = 6, shape = RoundedCornerShape(16.dp))
        }
        SectionCard(title = "Plan", subtitle = "Choose a priority, date, and realistic time estimate.") {
            FocusText("PRIORITY", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold, letterSpacing = .8.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                listOf("Low", "Medium", "High").forEach { option ->
                    FilterChip(selected = priority == option, onClick = { priority = option }, label = { FocusText(option) })
                }
            }
            OutlinedTextField(value = estimate, onValueChange = { value -> if (value.all(Char::isDigit)) estimate = value }, modifier = Modifier.fillMaxWidth(), label = { FocusText("Time estimate in minutes") }, singleLine = true, shape = RoundedCornerShape(16.dp), supportingText = { if (estimateValue == null || estimateValue !in 5..1440) FocusText("Choose between 5 and 1,440 minutes") })
            Surface(
                onClick = { showDatePicker = true },
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Column(Modifier.weight(1f)) {
                        FocusText("Due date (optional)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                        FocusText(
                            dueDate.takeIf { it.isNotBlank() }?.let { iso -> isoDateToEpochMillis(iso)?.let { formatEpochDate(it, LocalAppLanguage.current) } } ?: "Choose a date",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (dueDate.isNotBlank()) IconButton(onClick = { dueDate = "" }) { Icon(Icons.Filled.Close, contentDescription = localizedText("Clear date", LocalAppLanguage.current)) }
                }
            }
        }
        SectionCard(title = "Steps", subtitle = "Break the task into smaller wins when it helps.") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FocusText(if (subtasks.isEmpty()) "No steps yet" else "${subtasks.size} steps", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f))
                TextButton(onClick = { subtasks.add(TaskStepDraft(null, "", false)) }) { Icon(Icons.Filled.Add, null); Spacer(Modifier.width(4.dp)); FocusText("Add step") }
            }
            if (loadingSubtasks) CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
            subtasks.forEachIndexed { index, step ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = step.completed, onCheckedChange = { checked -> subtasks[index] = step.copy(completed = checked) })
                    OutlinedTextField(value = step.title, onValueChange = { value -> subtasks[index] = step.copy(title = value) }, modifier = Modifier.weight(1f), placeholder = { FocusText("Step ${index + 1}") }, singleLine = true, shape = RoundedCornerShape(14.dp))
                    IconButton(onClick = { subtasks.removeAt(index) }) { Icon(Icons.Filled.DeleteOutline, contentDescription = localizedText("Remove step ${index + 1}", LocalAppLanguage.current), tint = MaterialTheme.colorScheme.secondary) }
                }
            }
        }
        saveError?.let { FocusText(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
      }
      Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { FocusText("Cancel") }
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
                modifier = Modifier.weight(1.5f),
                enabled = title.isNotBlank() && estimateValue != null && estimateValue in 5..1440 && !loadingSubtasks && !saving,
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 15.dp)
            ) { if (saving) CircularProgressIndicator(Modifier.size(17.dp), strokeWidth = 2.dp) else FocusText(if (task == null) "Add task" else "Save changes") }
      }
    }
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = isoDateToEpochMillis(dueDate.takeIf { it.isNotBlank() }))
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dueDate = epochMillisToIsoDate(datePickerState.selectedDateMillis).orEmpty()
                    showDatePicker = false
                }, enabled = datePickerState.selectedDateMillis != null) { FocusText("Select date") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { FocusText("Cancel") } },
            shape = RoundedCornerShape(28.dp)
        ) { DatePicker(state = datePickerState, showModeToggle = false) }
    }
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
