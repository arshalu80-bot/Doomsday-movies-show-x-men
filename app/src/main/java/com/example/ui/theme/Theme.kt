package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DoomsColorScheme = darkColorScheme(
    primary = AccentDoom,
    onPrimary = Color(0xFF00381B),
    primaryContainer = Color(0xFF00522B),
    onPrimaryContainer = AccentDoom,
    secondary = AccentSeries,
    onSecondary = Color(0xFF003544),
    secondaryContainer = Color(0xFF004D63),
    onSecondaryContainer = AccentSeries,
    tertiary = AccentWatched,
    onTertiary = Color(0xFF3B0068),
    tertiaryContainer = Color(0xFF550091),
    onTertiaryContainer = AccentWatched,
    background = BgDark,
    onBackground = TextPrimary,
    surface = CardBg,
    onSurface = TextPrimary,
    surfaceVariant = CardBgElevated,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve high-contrast Doom cyberpunk branding
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DoomsColorScheme,
        typography = Typography,
        content = content
    )
}
