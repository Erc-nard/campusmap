package com.example.campusmap.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.campusmap.R

// Set of Material typography styles to start with

val PretendardFamily = FontFamily(
    // Thin (100)
    Font(R.font.pretendard_thin, FontWeight.Thin),
    // ExtraLight (200)
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight),
    // Light (300)
    Font(R.font.pretendard_light, FontWeight.Light),
    // Regular (400)
    Font(R.font.pretendard_regular, FontWeight.Normal), // FontWeight.Normal은 400입니다
    // Medium (500)
    Font(R.font.pretendard_medium, FontWeight.Medium),
    // SemiBold (600)
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    // Bold (700)
    Font(R.font.pretendard_bold, FontWeight.Bold),
    // ExtraBold (800)
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
    // Black (900)
    Font(R.font.pretendard_black, FontWeight.Black)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Medium, // Medium (500)
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Medium, // Medium (500)
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Medium, // Medium (500)
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal, // Regular (400)
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal, // Regular (400)
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal, // Regular (400)
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Medium, // Medium (500)
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Medium, // Medium (500)
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Medium, // Medium (500)
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)