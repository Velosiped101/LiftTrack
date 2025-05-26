package com.velosiped.notes.presentation.screens.settingsScreen

import com.velosiped.notes.utils.ThemeMode
import com.velosiped.notes.utils.CalorieSurplus
import com.velosiped.notes.utils.DailyActivityK
import com.velosiped.notes.utils.Sex

data class SettingsUiState(
    val appThemeMode: ThemeMode = ThemeMode.System,
    val bodyMass: Float = 55f,
    val height: Int = 169,
    val age: Int = 26,
    val sex: Sex = Sex.Male,
    val resetTimeHour: Int = 0,
    val resetTimeMinute: Int = 0,
    val dailyActivityK: DailyActivityK = DailyActivityK.Medium,
    val calorieSurplus: CalorieSurplus = CalorieSurplus.Maintenance,
    val isNotFirstLaunch: Boolean? = null
) {
    val autoTargetCalories: Int
        get() = when (sex) {
            Sex.Male -> (10 * bodyMass + 6.25 * height - 5 * age + 5) * dailyActivityK.k + calorieSurplus.cals
            Sex.Female -> (10 * bodyMass + 6.25 * height - 5 * age - 161) * dailyActivityK.k + calorieSurplus.cals
        }.toInt()
}