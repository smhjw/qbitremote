package com.hjw.qbremote.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val QbDarkColors = darkColorScheme(
    primary = Color(0xFF4FC3F7),
    onPrimary = Color(0xFF002634),
    secondary = Color(0xFF83E1D7),
    onSecondary = Color(0xFF003731),
    background = Color(0xFF101216),
    onBackground = Color(0xFFE9EFF3),
    surface = Color(0xFF12161A),
    onSurface = Color(0xFFE3E8EC),
    surfaceContainer = Color(0xFF1A2026),
    surfaceContainerHigh = Color(0xFF1E252B),
    primaryContainer = Color(0xFF16333F),
    secondaryContainer = Color(0xFF1E3A37),
)

@Composable
fun QBRemoteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = QbDarkColors,
        content = content,
    )
}
