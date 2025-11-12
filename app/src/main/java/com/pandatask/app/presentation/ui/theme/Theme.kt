package com.pandatask.app.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Custom color scheme matching PWA design
private val DarkColorScheme = darkColorScheme(
    primary = PandaGreen,
    onPrimary = Color.White,
    primaryContainer = PandaGreenDark,
    onPrimaryContainer = Color.White,
    
    secondary = PandaGreenVariant,
    onSecondary = Color.White,
    secondaryContainer = PandaGreenLight,
    onSecondaryContainer = Color.White,
    
    tertiary = PandaGreenLight,
    onTertiary = Color.White,
    
    background = DarkBackground,
    onBackground = DarkOnBackground,
    
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    
    outline = DarkOutline,
    outlineVariant = Color(0xFF525252), // neutral-600
    
    error = ErrorColor,
    onError = Color.White,
    errorContainer = ErrorSurface,
    onErrorContainer = ErrorColor,
    
    surfaceTint = PandaGreen
)

// Custom colors not covered by Material3
data class ExtendedColors(
    val success: Color = SuccessColor,
    val warning: Color = WarningColor,
    val fabBackground: Color = FabBackground,
    val fabContent: Color = FabContent,
    val progressBackground: Color = ProgressBackground,
    val progressIndicator: Color = ProgressIndicator,
    val selectionBackground: Color = SelectionBackground,
    val selectionBorder: Color = SelectionBorder
)

val LocalExtendedColors = staticCompositionLocalOf { ExtendedColors() }

@Composable
fun PandaTaskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // We use custom colors, not dynamic
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // Always use dark theme like PWA
    val extendedColors = ExtendedColors()

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

// Extension to access custom colors
val MaterialTheme.extendedColors: ExtendedColors
    @Composable get() = LocalExtendedColors.current