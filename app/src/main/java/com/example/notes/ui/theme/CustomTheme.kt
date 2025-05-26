package com.example.notes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColors(
    val proteinColor: Color,
    val fatColor: Color,
    val carbsColor: Color,
    val textSelectionHandleColor: Color,
    val textSelectionBackgroundColor: Color,
    val selectedOptionColor: Color,
    val notAchievedColor: Color,
    val littleAchievedColor: Color,
    val almostAchievedColor: Color,
    val achievedColor: Color,
    val volumeColor: Color,
    val oneRepMaxColor: Color,
    val avgRepsDoneColor: Color,
    val avgRepsPlannedColor: Color,
    val avgWeightDoneColor: Color
)

private val lightColors = CustomColors(
    proteinColor = ProteinColorLight,
    fatColor = FatColorLight,
    carbsColor = CarbsColorLight,
    textSelectionHandleColor = TextSelectionHandleLight,
    textSelectionBackgroundColor = TextSelectionBackgroundLight,
    selectedOptionColor = SelectedOptionLight,
    notAchievedColor = NotAchievedLight,
    littleAchievedColor = LittleAchievedLight,
    almostAchievedColor = AlmostAchievedLight,
    achievedColor = AchievedLight,
    volumeColor = VolumeColorLight,
    oneRepMaxColor = OneRepMaxColorLight,
    avgRepsDoneColor = AvgRepsColorLight,
    avgRepsPlannedColor = AvgPlannedRepsColorLight,
    avgWeightDoneColor = AvgWeightColorLight
)

private val darkColors = CustomColors(
    proteinColor = ProteinColorDark,
    fatColor = FatColorDark,
    carbsColor = CarbsColorDark,
    textSelectionHandleColor = TextSelectionHandleDark,
    textSelectionBackgroundColor = TextSelectionBackgroundDark,
    selectedOptionColor = SelectedOptionDark,
    notAchievedColor = NotAchievedDark,
    littleAchievedColor = LittleAchievedDark,
    almostAchievedColor = AlmostAchievedDark,
    achievedColor = AchievedDark,
    volumeColor = VolumeColorDark,
    oneRepMaxColor = OneRepMaxColorDark,
    avgRepsDoneColor = AvgRepsColorDark,
    avgRepsPlannedColor = AvgPlannedRepsColorDark,
    avgWeightDoneColor = AvgWeightColorDark
)

private val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        proteinColor = Color.Unspecified,
        fatColor = Color.Unspecified,
        carbsColor = Color.Unspecified,
        textSelectionHandleColor = Color.Unspecified,
        textSelectionBackgroundColor = Color.Unspecified,
        selectedOptionColor = Color.Unspecified,
        notAchievedColor = Color.Unspecified,
        littleAchievedColor = Color.Unspecified,
        almostAchievedColor = Color.Unspecified,
        achievedColor = Color.Unspecified,
        volumeColor = Color.Unspecified,
        oneRepMaxColor = Color.Unspecified,
        avgRepsDoneColor = Color.Unspecified,
        avgRepsPlannedColor = Color.Unspecified,
        avgWeightDoneColor = Color.Unspecified
    )
}

private val DarkColorScheme = darkColorScheme(
    surface = SurfaceDark,
    surfaceTint = IconTintDark,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = SurfaceDark,
    outline = OutlineDark,
    surfaceContainer = DialogDark,
    error = ErrorOutline,
    errorContainer = ErrorFilling.copy(alpha = 0.3f),
    secondaryContainer = GreyDark,
    primary = GreyDark
)

private val LightColorScheme = lightColorScheme(
    surface = SurfaceLight,
    surfaceTint = IconTintLight,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = SurfaceLight,
    outline = OutlineLight,
    surfaceContainer = DialogLight,
    error = ErrorOutline,
    errorContainer = ErrorFilling.copy(alpha = 0.3f),
    secondaryContainer = GreyLight,
    primary = GreyLight
)

@Composable
fun CustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val customColors = when {
        darkTheme -> darkColors
        else -> lightColors
    }
    
    val textSelectionColors = TextSelectionColors(
        handleColor = customColors.textSelectionHandleColor,
        backgroundColor = customColors.textSelectionBackgroundColor
    )

    CompositionLocalProvider(
        LocalCustomColors provides customColors,
        LocalTextSelectionColors provides textSelectionColors
    ) {
        val colorScheme = when {
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object CustomTheme {
    val colors: CustomColors
        @Composable
        get() = LocalCustomColors.current
}