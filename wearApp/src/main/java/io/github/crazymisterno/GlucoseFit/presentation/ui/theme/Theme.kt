package io.github.crazymisterno.GlucoseFit.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

private val DarkColorScheme = Colors(
    primary = DarkBgBlue,
    secondary = DarkBgGreen,
    surface = DarkCardBg,
    onSurface = Color.White
)

private val LightColorScheme = Colors(
    primary = LightBgBlue,
    secondary = LightBgGreen,
    surface = LightCardBg,
    onSurface = Color.Black
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
fun GlucoseFitTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    /**
     * Empty theme to customize for your app.
     * See: https://developer.android.com/jetpack/compose/designsystems/custom
     */
    MaterialTheme(
        colors = if (darkMode) DarkColorScheme else LightColorScheme,
        content = content
    )
}