package com.velosiped.settings.presentation.utils.mappers

import com.velosiped.notes.data.datastore.ProtoThemeMode
import com.velosiped.ui.utils.ThemeMode

fun ProtoThemeMode.toAppThemeMode(): ThemeMode = when (this) {
    ProtoThemeMode.System -> ThemeMode.SYSTEM
    ProtoThemeMode.Dark -> ThemeMode.DARK
    ProtoThemeMode.Light -> ThemeMode.LIGHT
    ProtoThemeMode.UNRECOGNIZED -> ThemeMode.SYSTEM
}

fun ThemeMode.toProtoThemeMode(): ProtoThemeMode = when (this) {
    ThemeMode.SYSTEM -> ProtoThemeMode.System
    ThemeMode.DARK -> ProtoThemeMode.Dark
    ThemeMode.LIGHT -> ProtoThemeMode.Light
}