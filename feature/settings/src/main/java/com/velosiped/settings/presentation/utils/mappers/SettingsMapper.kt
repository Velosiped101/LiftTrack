package com.velosiped.settings.presentation.utils.mappers

import com.velosiped.notes.data.datastore.AppPreferences
import com.velosiped.settings.presentation.SettingsUiState

fun AppPreferences.toSettingsUiState() = SettingsUiState(
    appThemeMode = this.themeMode.toAppThemeMode(),
    bodyMass = this.bodyMass,
    height = this.height,
    age = this.age,
    sex = this.sex.toSex(),
    dailyActivity = this.dailyActivityK.toDailyActivity(),
    goal = this.calorieSurplus.toGoal(),
    resetTime = this.resetTime.toResetTime(),
    isNotFirstLaunch = this.isNotFirstLaunch
)

fun SettingsUiState.toAppPreferences() = AppPreferences
    .newBuilder()
    .setThemeMode(this.appThemeMode.toProtoThemeMode())
    .setBodyMass(this.bodyMass)
    .setHeight(this.height)
    .setAge(this.age)
    .setSex(this.sex.toProtoSex())
    .setDailyActivityK(this.dailyActivity.toProtoDailyActivity())
    .setCalorieSurplus(this.goal.toProtoCalorieSurplus())
    .setResetTime(this.resetTime.toProtoResetTime())
    .setIsNotFirstLaunch(true)
    .setTargetCalories(this.autoTargetCalories)
    .build()