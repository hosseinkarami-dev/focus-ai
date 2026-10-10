package io.github.hosseinkarami_dev.focusai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val lightColors = lightColorScheme(
    primary = Color(0xFF5954C8),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE5E4FF),
    onPrimaryContainer = Color(0xFF25205F),
    secondary = Color(0xFF64627A),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE9E7F2),
    onSecondaryContainer = Color(0xFF302F42),
    tertiary = Color(0xFFD66B55),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE3DA),
    onTertiaryContainer = Color(0xFF4B2118),
    error = Color(0xFFB33D36),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD5),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF5F4FA),
    onBackground = Color(0xFF201F2D),
    surface = Color(0xFFFFFEFF),
    onSurface = Color(0xFF201F2D),
    surfaceVariant = Color(0xFFECEBF3),
    onSurfaceVariant = Color(0xFF484657),
    outline = Color(0xFF777487),
    outlineVariant = Color(0xFFD5D3E0),
    scrim = Color(0xFF000000)
)

private val darkColors = darkColorScheme(
    primary = Color(0xFFB9B4FF),
    onPrimary = Color(0xFF211B5E),
    primaryContainer = Color(0xFF3B367C),
    onPrimaryContainer = Color(0xFFE5E2FF),
    secondary = Color(0xFFC7C4D8),
    onSecondary = Color(0xFF302F42),
    secondaryContainer = Color(0xFF454355),
    onSecondaryContainer = Color(0xFFE3E0F4),
    tertiary = Color(0xFFFFB49C),
    onTertiary = Color(0xFF542116),
    tertiaryContainer = Color(0xFF753A2B),
    onTertiaryContainer = Color(0xFFFFDBD0),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF141320),
    onBackground = Color(0xFFE8E6F3),
    surface = Color(0xFF1D1B2A),
    onSurface = Color(0xFFE8E6F3),
    surfaceVariant = Color(0xFF292737),
    onSurfaceVariant = Color(0xFFC8C5D7),
    outline = Color(0xFF918EA3),
    outlineVariant = Color(0xFF403E4F),
    scrim = Color(0xFF000000)
)

@Composable
fun FocusTheme(themeMode: String = "System", content: @Composable () -> Unit) {
    val useDarkTheme = when (themeMode) {
        "Light" -> false
        "Dark" -> true
        else -> isSystemInDarkTheme()
    }
    MaterialTheme(colorScheme = if (useDarkTheme) darkColors else lightColors, content = content)
}
