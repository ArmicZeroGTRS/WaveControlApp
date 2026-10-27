package com.wavecontrol.app.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val KineticDarkColorScheme = darkColorScheme(
    primary = LuminousCyan,
    onPrimary = Color(0xFF00363A),
    primaryContainer = LuminousCyan,
    onPrimaryContainer = Color(0xFF00363A),
    secondary = CyberTeal,
    onSecondary = Color(0xFF003732),
    secondaryContainer = CyberTealDark,
    onSecondaryContainer = Color.White,
    tertiary = ElectricVioletLight,
    onTertiary = Color(0xFF3F008E),
    tertiaryContainer = ElectricViolet,
    background = KineticBackground,
    onBackground = OnSurface,
    surface = KineticSurface,
    onSurface = OnSurface,
    surfaceVariant = KineticSurfaceHigh,
    onSurfaceVariant = OnSurfaceVariant,
    outline = HairlineBorder,
    outlineVariant = HairlineBorderVariant,
    error = TerminalRed,
    onError = Color.White
)

private val KineticLightColorScheme = lightColorScheme(
    primary = CyberTealDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF7DF4FF),
    onPrimaryContainer = Color(0xFF002022),
    secondary = Color(0xFF0D9488),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCFBF1),
    onSecondaryContainer = Color(0xFF134E4A),
    tertiary = ElectricViolet,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceHigh,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFF94A3B8),
    error = TerminalRed,
    onError = Color.White
)

@Composable
fun WaveControlTheme(
    themeMode: String = "dark",
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode.lowercase()) {
        "light" -> false
        "system" -> isSystemInDarkTheme()
        else -> true // default to pure obsidian kinetic dark
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> KineticDarkColorScheme
        else -> KineticLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
