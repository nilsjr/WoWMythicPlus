package de.nilsdruyen.mythicplus.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
  background = background,
  onBackground = background800,
  primary = purplePrimary,
  secondary = purpleSecondary,
  onPrimary = Color.White,
  onSecondary = Color.White,
)

//private val LightColorPalette = lightColorScheme(
//  background = Color.White,
//  onBackground = Color.White,
//  surface = Color.White,
//  primary = purple200,
//  secondary = purple500,
//  onPrimary = Color.White,
//  onSecondary = Color.White
//)

@Composable
fun MythicPlusTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
//  val colors = if (darkTheme) {
//    DarkColorPalette
//  } else {
//    LightColorPalette
//  }
//  val typography = if (darkTheme) {
//    DarkTypography
//  } else {
//    LightTypography
//  }

  MaterialTheme(
    colorScheme = DarkColorPalette,
    typography = DarkTypography,
    content = content
  )
}