package com.example.notes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val Typography.input: TextStyle
    get() = TextStyle(
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Default,
        fontSize = 20.sp
    )

val Typography.ingredientNameInput: TextStyle
    @Composable
    get() = TextStyle(
        textAlign = TextAlign.Start,
        fontFamily = FontFamily.Default,
        fontSize = 26.sp,
        color = MaterialTheme.colorScheme.onSurface
    )

val Typography.ingredientNutrientInput: TextStyle
    @Composable
    get() = TextStyle(
        textAlign = TextAlign.Start,
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurface
    )

val Typography.underlineHint: TextStyle
    @Composable
    get() = TextStyle(
        textAlign = TextAlign.Start,
        fontFamily = FontFamily.Default,
        fontSize = 9.sp,
        color = MaterialTheme.colorScheme.onSurface
    )