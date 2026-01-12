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
    bodyLarge = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)