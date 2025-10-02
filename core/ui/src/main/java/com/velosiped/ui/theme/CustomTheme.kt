package com.velosiped.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalCustomColors = staticCompositionLocalOf { lightColors }
val LocalCustomTypography = staticCompositionLocalOf { CustomTypography() }

@Composable
fun CustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    customTypography: CustomTypography = CustomTheme.typography,
    content: @Composable () -> Unit
) {
    val customColors = when {
        darkTheme -> darkColors
        else -> lightColors
    }

    CompositionLocalProvider(
        LocalCustomColors provides customColors,
        LocalCustomTypography provides customTypography
    ) {
        content()
    }
}

object CustomTheme {
    val colors: CustomColors
        @Composable
        get() = LocalCustomColors.current

    val typography: CustomTypography
        @Composable
        get() = LocalCustomTypography.current
}