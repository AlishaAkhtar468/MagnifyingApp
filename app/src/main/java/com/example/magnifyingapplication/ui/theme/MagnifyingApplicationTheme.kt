package com.example.magnifyingapplication.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.isSystemInDarkTheme


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0E2C66),
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color(0xFF0E2C66),
    surface = Color.White,
    surfaceVariant = Color(0xFF0E2C66)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0E2C66),
    onPrimary = Color.Black,
    background = Color(0xFF919294),
    onBackground = Color.White,
    surface = Color(0xFF1A1A1A),
    surfaceVariant = Color.White
)

@Composable
fun MagnifyingApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val headingColor = if (darkTheme) Color.White else Color(0xFF7897D2)

    val appTypography = Typography(
        headlineLarge = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Black,
            fontSize = 24.sp,
            color = headingColor
        ),
        bodyLarge = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = colorScheme.onBackground
        ),
        bodyMedium = TextStyle(
            fontFamily = DMSans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = colorScheme.onBackground
        ),
        labelLarge = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Black,
            fontSize = 15.sp,
            color = colorScheme.primary
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography,
        content = content
    )
}
