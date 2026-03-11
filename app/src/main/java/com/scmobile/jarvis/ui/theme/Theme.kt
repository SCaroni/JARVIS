package com.scmobile.jarvis.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val JarvisColorScheme = darkColorScheme(

    background = TerminalBackground,
    surface = TerminalBackground,

    primary = TerminalGreen,
    secondary = TerminalGreenSoft,

    onBackground = TerminalText,
    onSurface = TerminalText

)

@Composable
fun JarvisTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = JarvisColorScheme,
        typography = JarvisTypography,
        content = content
    )
}