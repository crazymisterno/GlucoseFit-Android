package io.github.crazymisterno.GlucoseFit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkBgBlue,
    secondary = DarkBgGreen,
    surface = DarkCardBg,
    primaryContainer = DarkAccent,
    secondaryContainer = DarkSuccess,
    surfaceContainerHigh = LightBgBlue,
    surfaceContainerLow = LightBgGreen
)

private val LightColorScheme = lightColorScheme(
    primary = LightBgBlue,
    secondary = LightBgGreen,
    surface = LightCardBg,
    primaryContainer = LightAccent,
    secondaryContainer = LightSuccess,
    surfaceContainerHigh = DarkBgBlue,
    surfaceContainerLow = DarkBgGreen
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun GlucoseFitMaterialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}