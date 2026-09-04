package com.example.ui.theme

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

private val SleekDarkColorScheme =
  darkColorScheme(
    primary = Stage1Emerald,
    onPrimary = TextWhite,
    primaryContainer = Stage1EmeraldContainer,
    onPrimaryContainer = TextWhite,
    secondary = Stage2Amber,
    onSecondary = CanvasNavy,
    secondaryContainer = Stage2AmberContainer,
    onSecondaryContainer = TextWhite,
    tertiary = Stage3Rose,
    onTertiary = TextWhite,
    tertiaryContainer = Stage3RoseContainer,
    onTertiaryContainer = TextWhite,
    background = CanvasNavy,
    onBackground = TextWhite,
    surface = CardNavy,
    onSurface = TextWhite,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSlate,
    outline = BorderNavy,
    outlineVariant = BorderLight
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force sleek dark safety theme
  dynamicColor: Boolean = false, // Keep high contrast brand colors
  content: @Composable () -> Unit,
) {
  val colorScheme = SleekDarkColorScheme

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      if (window != null) {
        window.statusBarColor = CanvasNavy.toArgb()
        window.navigationBarColor = CanvasNavy.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
      }
    }
  }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

