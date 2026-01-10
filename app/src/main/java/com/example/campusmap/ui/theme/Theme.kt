package com.example.campusmap.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.campusmap.ui.theme.*


private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = black,
    tertiary = Pink80,
    background = Color(0xFF1E1E1E),   // 다크 모드 배경
    surface = Color(0xFF1E1E1E),      // 카드, 표면
    onPrimary = white,           // primary 위 텍스트
    onSecondary = white,
    onBackground = white,        // 배경 위 텍스트
    onSurface = white
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = white,
    tertiary = Pink40,
    background = Color(0xFFFFFFFF),   // 밝은 배경
    surface = Color(0xFFFFFFFF),      // 카드, 표면
    onPrimary = white,
    onSecondary = black,
    onBackground = black,       // 배경 위 텍스트
    onSurface = black
)


@Composable
fun CampusmapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}