package de.nilsdruyen.mythicplus.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// set of dark material typography styles to start with.
val DarkTypography = Typography(
  headlineLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    color = Color.White,
    fontSize = 28.sp
  ),
  headlineMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    color = Color.White,
    fontSize = 21.sp
  ),
  bodyLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    color = Color.White,
    fontSize = 14.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    color = white87,
    fontSize = 14.sp
  )
)

// set of light material typography styles to start with.
val LightTypography = Typography(
  headlineLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    color = background900,
    fontSize = 28.sp
  ),
  headlineMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    color = background900,
    fontSize = 21.sp
  ),
  bodyLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    color = background800,
    fontSize = 14.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    color = background800,
    fontSize = 14.sp
  )
)