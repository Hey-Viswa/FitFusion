package com.viswa.fitfusion.ui.theme

import android.app.Activity
import android.graphics.Color.toArgb
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController


// Updated Color Palette inspired by the design in the screenshots
private val primaryLight = Color(0xFFBFFF00) // Neon-like bright green, used for emphasis
private val onPrimaryLight = Color.Black
private val backgroundLight = Color(0xFFFFFFFF) // Light theme background (white)
private val onBackgroundLight = Color(0xFF171D1E) // Dark color for text on light backgrounds
private val surfaceLight = Color(0xFFF5FAFB) // Light grey for surfaces
private val onSurfaceLight = Color.Black

private val primaryDark = Color(0xFFBFFF00) // Neon-like bright green, for dark mode accents
private val onPrimaryDark = Color.Black
private val backgroundDark = Color(0xFF121212) // Dark mode background (near-black)
private val onBackgroundDark = Color(0xFFE0E0E0) // Light grey for text on dark backgrounds
private val surfaceDark = Color(0xFF1C1C1C) // Darker grey for surfaces
private val onSurfaceDark = Color.White


// Updated light color scheme for fitness app with primary focus on highlights
private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    secondary = Color(0xFF4A4A4A), // Muted grey for inactive items
    onSecondary = Color.White,
    tertiary = Color(0xFF00E676), // Bright green for success indicators
    onTertiary = Color.Black,
    error = Color(0xFFCF6679),
    onError = Color.White
)

// Updated dark color scheme for fitness app with neon highlights
private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    secondary = Color(0xFFB1CBD0), // A light grey-blue used for secondary text or inactive items
    onSecondary = Color.Black,
    tertiary = Color(0xFF00E676), // Bright green for success indicators
    onTertiary = Color.Black,
    error = Color(0xFFFFB4AB),
    onError = Color.Black
)

@Composable
fun FitFusionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Dynamic color is available on Android 12+ (API level 31)
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    // Handle system UI colors for status bars, navigation bars, etc.
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = if (darkTheme) Color.Transparent else Color.White,
            darkIcons = useDarkIcons
        )
    }

    // Get the current view and change system UI colors (status bar)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb() // Set status bar color
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Assuming you have typography defined
        content = content
    )
}