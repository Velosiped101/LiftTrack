package com.example.notes.presentation.screens.settingsScreen

import com.example.notes.utils.ThemeMode
import com.example.notes.utils.CalorieSurplus
import com.example.notes.utils.DailyActivityK
import com.example.notes.utils.Sex

sealed interface SettingsUiAction {
    data class ChangeTheme(val theme: ThemeMode): SettingsUiAction
    data class ChangeBodyMass(val mass: Float): SettingsUiAction
    data class ChangeHeight(val height: Int): SettingsUiAction
    data class ChangeAge(val age: Int): SettingsUiAction
    data class ChangeSex(val sex: Sex): SettingsUiAction
    data class ChangeResetTimeHour(val hour: Int): SettingsUiAction
    data class ChangeResetTimeMinute(val minute: Int): SettingsUiAction
    data class ChangeDailyActivity(val dailyActivityK: DailyActivityK): SettingsUiAction
    data class ChangeCalorieSurplus(val calorieSurplus: CalorieSurplus): SettingsUiAction
    data object ConfirmSettings: SettingsUiAction
    data object RestoreState: SettingsUiAction
}