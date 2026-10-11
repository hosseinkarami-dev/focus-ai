package io.github.hosseinkarami_dev.focusai.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import io.github.hosseinkarami_dev.focusai.ui.FocusText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import io.github.hosseinkarami_dev.focusai.data.NoteEntity
import io.github.hosseinkarami_dev.focusai.data.TaskProposal
import io.github.hosseinkarami_dev.focusai.presentation.TodayViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CaptureDialog(
    existingNote: NoteEntity?,
    sendsNotesToBackend: Boolean,
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
                FocusText(
                    if (sendsNotesToBackend) "When you request suggestions, your note is sent to the configured FocusAI service and OpenAI. Review each suggestion before saving."
                    else "Sample AI preview · no service is contacted",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
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
                        } catch (_: Exception) {
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
                        } catch (_: Exception) {
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
internal fun ReviewDialog(proposals: List<TaskProposal>, onDismiss: () -> Unit, onConfirm: (List<TaskProposal>) -> Unit) {
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
