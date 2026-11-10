package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = RajputAccent,
    onPrimary = Color.White,
    primaryContainer = RajputDarkRed,
    onPrimaryContainer = Color.White,
    secondary = RajputCharcoal,
    onSecondary = Color.White,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF282828),
    onSurfaceVariant = Color(0xFFE0E0E0),
    outline = Color(0xFF3E3E3E)
)

private val LightColorScheme = lightColorScheme(
    primary = RajputRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFEBEE),
    onPrimaryContainer = RajputDarkRed,
    secondary = RajputCharcoal,
    onSecondary = Color.White,
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFFFFFF),
    onSurface = RajputTextDark,
    surfaceVariant = RajputLightGrey,
    onSurfaceVariant = RajputTextMuted,
    outline = RajputBorder
)

@Composable
fun RajputFastFoodTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    RajputFastFoodTheme(darkTheme = darkTheme, content = content)
}
