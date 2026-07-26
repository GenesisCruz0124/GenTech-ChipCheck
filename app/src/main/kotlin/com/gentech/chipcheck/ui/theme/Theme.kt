package com.gentech.chipcheck.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = GreenPrimary,
    primaryContainer = GreenPrimaryContainer,
    secondary = GreenSecondary
)

private val DarkColors = darkColorScheme(
    primary = GreenPrimaryContainer,
    secondary = GreenPrimaryContainer
)

@Composable
fun ChipCheckTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = ChipCheckTypography,
        content = content
    )
}
