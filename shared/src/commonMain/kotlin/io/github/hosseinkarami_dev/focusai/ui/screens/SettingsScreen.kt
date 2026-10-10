package io.github.hosseinkarami_dev.focusai.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text as MaterialText
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.hosseinkarami_dev.focusai.data.AppSettingsEntity
import io.github.hosseinkarami_dev.focusai.ui.FocusAiMark
import io.github.hosseinkarami_dev.focusai.ui.FocusText
import io.github.hosseinkarami_dev.focusai.ui.LocalAppLanguage
import io.github.hosseinkarami_dev.focusai.ui.SectionCard
import io.github.hosseinkarami_dev.focusai.ui.localizedText

@Composable
internal fun SettingsScreen(
    padding: PaddingValues,
    preferences: AppSettingsEntity,
    onThemeMode: (String) -> Unit,
    onFocusLength: (Int) -> Unit,
    onLanguage: () -> Unit
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
        SectionCard(title = "Appearance", subtitle = "Choose the look that feels right.") {
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
        SectionCard(title = "Language", subtitle = "Choose your app language.") {
            Surface(onClick = onLanguage, shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(38.dp)) {
                        Box(contentAlignment = Alignment.Center) { FocusText("Aa", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
                    }
                    Column(Modifier.weight(1f)) {
                        FocusText("App language", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                        MaterialText(languageDisplayName(preferences.language), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    }
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = localizedText("Choose language", LocalAppLanguage.current), tint = MaterialTheme.colorScheme.secondary)
                }
            }
        }
        SectionCard(title = "Focus session", subtitle = "Pick a comfortable starting length.") {
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

private fun languageDisplayName(code: String): String = when (code) {
    "fa" -> "فارسی"
    "es" -> "Español"
    "fr" -> "Français"
    "ar" -> "العربية"
    else -> "English"
}

@Composable
internal fun LanguagePickerDialog(selectedLanguage: String, onSelect: (String) -> Unit, onDismiss: () -> Unit) {
    val languages = listOf("en", "fa", "es", "fr", "ar")
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(48.dp)) {
                Box(contentAlignment = Alignment.Center) { FocusText("Aa", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium) }
            }
        },
        title = { FocusText("App language", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
        text = {
            Column(Modifier.fillMaxWidth().heightIn(max = 390.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FocusText("Choose your app language.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.height(4.dp))
                languages.forEach { code ->
                    val selected = code == selectedLanguage
                    Surface(
                        onClick = { onSelect(code) },
                        shape = RoundedCornerShape(16.dp),
                        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(horizontal = 8.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selected, onClick = { onSelect(code) })
                            Column(Modifier.weight(1f).padding(start = 6.dp)) {
                                MaterialText(languageDisplayName(code), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                                FocusText(when (code) { "en" -> "English"; "fa" -> "Persian"; "es" -> "Spanish"; "fr" -> "French"; else -> "Arabic" }, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                            }
                            if (selected) Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { FocusText("Done") } },
        shape = RoundedCornerShape(28.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}
