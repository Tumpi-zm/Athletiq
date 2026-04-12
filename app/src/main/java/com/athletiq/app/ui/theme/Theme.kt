package com.athletiq.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Athletiq Material 3 theme configuration.
 *
 * Provides both dark and light color schemes. Dark mode is the default gym experience,
 * but the theme respects the system setting. The status bar color and icon tint are
 * dynamically set to match the current theme.
 *
 * @param darkTheme Whether to use the dark color scheme. Defaults to system preference.
 * @param content The composable content wrapped in this theme.
 */

private val DarkColorScheme = darkColorScheme(
    primary = AthletiqPrimary,
    onPrimary = AthletiqOnPrimary,
    primaryContainer = AthletiqPrimaryContainer,
    onPrimaryContainer = AthletiqOnPrimaryContainer,
    secondary = AthletiqSecondary,
    onSecondary = AthletiqOnSecondary,
    secondaryContainer = AthletiqSecondaryContainer,
    onSecondaryContainer = AthletiqOnSecondaryContainer,
    tertiary = AthletiqTertiary,
    onTertiary = AthletiqOnTertiary,
    error = AthletiqError,
    onError = AthletiqOnError,
    background = AthletiqBackground,
    onBackground = AthletiqOnBackground,
    surface = AthletiqSurface,
    onSurface = AthletiqOnSurface,
    surfaceVariant = AthletiqSurfaceVariant,
    onSurfaceVariant = AthletiqOnSurfaceVariant,
    outline = AthletiqOutline
)

private val LightColorScheme = lightColorScheme(
    primary = AthletiqPrimary,
    onPrimary = AthletiqOnPrimary,
    primaryContainer = AthletiqOnPrimaryContainer,
    onPrimaryContainer = AthletiqPrimaryContainer,
    secondary = AthletiqSecondary,
    onSecondary = AthletiqOnSecondary,
    secondaryContainer = AthletiqOnSecondaryContainer,
    onSecondaryContainer = AthletiqSecondaryContainer,
    tertiary = AthletiqTertiary,
    onTertiary = AthletiqOnTertiary,
    error = AthletiqError,
    onError = AthletiqOnError,
    background = AthletiqBackgroundLight,
    onBackground = AthletiqOnBackgroundLight,
    surface = AthletiqSurfaceLight,
    onSurface = AthletiqOnSurfaceLight,
    surfaceVariant = AthletiqSurfaceVariantLight,
    onSurfaceVariant = AthletiqOnSurfaceVariantLight,
    outline = AthletiqOutlineLight
)

@Composable
fun AthletiqTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Update the system bar colors to match the theme.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AthletiqTypography,
        content = content
    )
}

// End of Theme.kt — Material 3 theme with dark/light schemes for Athletiq.
