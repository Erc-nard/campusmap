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
    primary = main, //다크모드따위는없다
    secondary = white,
    tertiary = dark,
    background = white,   // 밝은 배경
    surface = white,      // 카드, 표면
    onPrimary = main, //primary 위 텍스트색
    onSecondary = black,
    onBackground = black,       // 배경 위 텍스트
    onSurface = black
)

private val LightColorScheme = lightColorScheme(
    primary = main,
    secondary = white,
    tertiary = dark,
    background = white,   // 밝은 배경
    surface = white,      // 카드, 표면
    onPrimary = white, //primary 위 텍스트색
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