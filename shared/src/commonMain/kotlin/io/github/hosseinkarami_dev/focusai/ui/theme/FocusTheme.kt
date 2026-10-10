package io.github.hosseinkarami_dev.focusai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

private val Pine = Color(0xFF285C48)
private val Canvas = Color(0xFFF6F7F2)
private val Ink = Color(0xFF19231D)
private val Night = Color(0xFF171D19)
private val Mint = Color(0xFFB8E0C7)

@Composable
fun FocusTheme(content: @Composable () -> Unit) {
    val scheme = lightColorScheme(
        primary = Pine,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFE1F0E5),
        secondary = Color(0xFF64786D),
        background = Canvas,
        surface = Color.White,
        surfaceVariant = Color(0xFFEDF1EC),
        onBackground = Ink,
        onSurface = Ink,
        outline = Color(0xFFDCE3DD),
        error = Color(0xFFB5483E)
    )
    val dark = darkColorScheme(
        primary = Mint,
        onPrimary = Color(0xFF183225),
        primaryContainer = Color(0xFF294638),
        secondary = Color(0xFFB6C8BC),
        background = Night,
        surface = Color(0xFF202823),
        surfaceVariant = Color(0xFF29332D),
        onBackground = Color(0xFFEEF3EF),
        onSurface = Color(0xFFEEF3EF),
        outline = Color(0xFF3D4941),
        error = Color(0xFFFFB4AA)
    )
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) dark else scheme, content = content)
}
