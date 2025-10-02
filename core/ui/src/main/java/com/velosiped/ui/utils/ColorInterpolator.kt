package com.velosiped.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

fun interpolateFourColors(progress: Float, colors: List<Color>): Color {
    val segment = when {
        progress < .33f -> 0
        progress < .66f -> 1
        else -> 2
    }
    val localProgress = when (segment) {
        0 -> progress / .33f
        1 -> (progress - .33f) / (.33f)
        else -> (progress - .66f) / (.33f)
    }
    val colorStart = colors[segment]
    val colorEnd = colors[segment + 1]
    return lerp(colorStart, colorEnd, localProgress)
}