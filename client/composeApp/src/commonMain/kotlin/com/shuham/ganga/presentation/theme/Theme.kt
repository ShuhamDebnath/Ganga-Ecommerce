package com.shuham.ganga.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- COLORS ---
val GangaOrange = Color(0xFFF35C22)
val GangaOrangeLight = Color(0xFFFFF0EB)
val GangaBlack = Color(0xFF1A1A1A)
val GangaGrey = Color(0xFF757575)
val GangaBackground = Color(0xFFF9F9F9)
val GangaSurface = Color(0xFFFFFFFF)

private val LightColorScheme = lightColorScheme(
    primary = GangaOrange,
    onPrimary = Color.White,
    secondary = GangaOrangeLight,
    onSecondary = GangaOrange,
    background = GangaBackground,
    surface = GangaSurface,
    onSurface = GangaBlack,
    onSurfaceVariant = GangaGrey,
    outline = Color(0xFFE0E0E0)
)

// --- TYPOGRAPHY ---
val GangaTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// --- SHAPES ---
val GangaShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

@Composable
fun GangaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // For now, we stick to Light Theme to match the reference images
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = GangaTypography,
        shapes = GangaShapes,
        content = content
    )
}