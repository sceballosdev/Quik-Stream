package com.sceballosdev.quikstream.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = QuikBlue,
    onPrimary = Color.White,
    primaryContainer = QuikLightBlue.copy(alpha = 0.20f),
    onPrimaryContainer = Color.Black,

    secondary = QuikLightBlue,
    onSecondary = Color.White,
    secondaryContainer = QuikLightBlue.copy(alpha = 0.20f),
    onSecondaryContainer = QuikLightBlue,

    background = BackgroundLight,
    onBackground = Color.Black,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = ItemBg,
    onSurfaceVariant = OnSurfaceLight.copy(alpha = 0.80f),

    error = Color(0xFFB00020),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = QuikLightBlue,
    onPrimary = Color.Black,
    primaryContainer = QuikLightBlue.copy(alpha = 0.20f),
    onPrimaryContainer = Color.White,

    secondary = QuikDarkBlue,
    onSecondary = Color.Black,
    secondaryContainer = QuikDarkBlue.copy(alpha = 0.20f),
    onSecondaryContainer = QuikDarkBlue,

    background = BackgroundDark,
    onBackground = QuikLightBlue,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = Color(0xFF2A2F36),
    onSurfaceVariant = OnSurfaceDark.copy(alpha = 0.80f),

    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun QuikStreamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when {
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = QuikStreamTypography,
        shapes = QuikStreamShapes,
        content = content
    )
}
