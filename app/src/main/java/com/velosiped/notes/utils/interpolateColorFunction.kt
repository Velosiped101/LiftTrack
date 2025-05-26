package com.velosiped.notes.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

fun interpolateColors(t: Float, colors: List<Color>): Color {
    val segment = when {
        t < .33f -> 0
        t < .66f -> 1
        else -> 2
    }
    val localT = when (segment) {
        0 -> t / .33f
        1 -> (t - .33f) / (.33f)
        else -> (t - .66f) / (.33f)
    }
    val colorStart = colors[segment]
    val colorEnd = colors[segment + 1]
    return lerp(colorStart, colorEnd, localT)
}