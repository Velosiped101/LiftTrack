package com.velosiped.settings.presentation

import com.velosiped.settings.presentation.utils.DailyActivity
import com.velosiped.settings.presentation.utils.Goal
import com.velosiped.settings.presentation.utils.ResetTime
import com.velosiped.settings.presentation.utils.Sex
import com.velosiped.ui.utils.ThemeMode
import com.velosiped.utility.extensions.ZERO

data class SettingsUiState(
    val appThemeMode: ThemeMode = ThemeMode.SYSTEM,
    val bodyMass: Float = 72f,
    val height: Int = 169,
    val age: Int = 25,
    val sex: Sex = Sex.MALE,
    val resetTime: ResetTime = ResetTime(Int.ZERO, Int.ZERO),
    val dailyActivity: DailyActivity = DailyActivity.MEDIUM,
    val goal: Goal = Goal.GAIN,
    val isNotFirstLaunch: Boolean? = null
) {
    val autoTargetCalories: Int
        get() = when (sex) {
            Sex.MALE -> (10 * bodyMass + 6.25 * height - 5 * age + 5) * dailyActivity.k + goal.cals
            Sex.FEMALE -> (10 * bodyMass + 6.25 * height - 5 * age - 161) * dailyActivity.k + goal.cals
        }.toInt()
}