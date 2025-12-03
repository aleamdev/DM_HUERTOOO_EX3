package com.dewis.dm_huertohogar_ex3.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores base de la marca
private val BrandGreen = Color(0xFF2E8B57)
private val BrandGreenDark = Color(0xFF1F5E3B)
private val BrandAccent = Color(0xFFFFC107)
private val BrandSurfaceLight = Color(0xFFF5FFF8)
private val BrandSurfaceDark = Color(0xFF101814)

private val LightColors = lightColorScheme(
    primary = BrandGreen,
    onPrimary = Color.White,
    primaryContainer = BrandGreen.copy(alpha = 0.9f),
    onPrimaryContainer = Color.White,

    secondary = BrandAccent,
    onSecondary = Color.Black,

    background = BrandSurfaceLight,
    onBackground = Color(0xFF102019),

    surface = Color.White,
    onSurface = Color(0xFF102019),

    surfaceVariant = Color(0xFFE1F0E7),
    onSurfaceVariant = Color(0xFF304238),

    error = Color(0xFFB3261E),
    onError = Color.White,

    outline = BrandGreen.copy(alpha = 0.4f)
)

private val DarkColors = darkColorScheme(
    primary = BrandGreen,
    onPrimary = Color.White,
    primaryContainer = BrandGreenDark,
    onPrimaryContainer = Color.White,

    secondary = BrandAccent,
    onSecondary = Color.Black,

    background = BrandSurfaceDark,
    onBackground = Color(0xFFE2F5EC),

    surface = Color(0xFF18221D),
    onSurface = Color(0xFFE2F5EC),

    surfaceVariant = Color(0xFF26352D),
    onSurfaceVariant = Color(0xFFD0E4DA),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),

    outline = BrandGreen.copy(alpha = 0.5f)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val scheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = scheme,
        typography = Typography,
        content = content
    )
}
