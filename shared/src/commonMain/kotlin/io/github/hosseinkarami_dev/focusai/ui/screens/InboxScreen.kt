package io.github.hosseinkarami_dev.focusai.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import io.github.hosseinkarami_dev.focusai.ui.FocusText
import io.github.hosseinkarami_dev.focusai.formatEpochDate
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
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import io.github.hosseinkarami_dev.focusai.data.NoteEntity
import io.github.hosseinkarami_dev.focusai.ui.LocalAppLanguage
import io.github.hosseinkarami_dev.focusai.ui.localizedText

@Composable
internal fun InboxScreen(notes: List<NoteEntity>, padding: PaddingValues, onOpenNote: (NoteEntity) -> Unit, onDeleteNote: (NoteEntity) -> Unit, onArchiveNote: (NoteEntity, Boolean) -> Unit) {
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
                                        contentDescription = localizedText(
                                            if (note.archived) "Restore note" else "Archive note",
                                            LocalAppLanguage.current
                                        ),
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
