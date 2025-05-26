package com.velosiped.notes.utils

import com.velosiped.notes.R
import com.velosiped.notes.proto.ProtoThemeMode

enum class ThemeMode(override val textId: Int): TextRepresentable {
    System(R.string.theme_system),
    Dark(R.string.theme_dark),
    Light(R.string.theme_light)
}

fun ThemeMode.toProto(): ProtoThemeMode {
    return when (this) {
        ThemeMode.System -> ProtoThemeMode.System
        ThemeMode.Dark -> ProtoThemeMode.Dark
        ThemeMode.Light -> ProtoThemeMode.Light
    }
}

fun ProtoThemeMode.toAppThemeMode(): ThemeMode {
    return when (this) {
        ProtoThemeMode.System -> ThemeMode.System
        ProtoThemeMode.Dark -> ThemeMode.Dark
        ProtoThemeMode.Light -> ThemeMode.Light
        ProtoThemeMode.UNRECOGNIZED -> ThemeMode.System
    }
}