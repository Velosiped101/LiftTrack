package com.velosiped.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun SettingsScreenWrapper(
    viewModel: SettingsViewModel,
    onNavigateToMainScreen: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.settingsSaved.collect {
            onNavigateToMainScreen()
        }
    }

    SettingsScreen(
        isNotFirstLaunch = uiState.isNotFirstLaunch,
        currentTheme = uiState.appThemeMode,
        resetTime = uiState.resetTime,
        sex = uiState.sex,
        age = uiState.age,
        height = uiState.height,
        bodyMass = uiState.bodyMass,
        dailyActivity = uiState.dailyActivity,
        goal = uiState.goal,
        autoTargetCalories = uiState.autoTargetCalories,
        onRestoreState = viewModel::restoreState,
        onThemeChange = viewModel::changeTheme,
        onResetTimeChange = viewModel::changeResetTimeHour,
        onSexChange = viewModel::changeSex,
        onAgeChange = viewModel::changeAge,
        onHeightChange = viewModel::changeHeight,
        onBodyMassChange = viewModel::changeBodyMass,
        onDailyActivityChange = viewModel::changeDailyActivityK,
        onGoalChange = viewModel::changeGoal,
        onConfirm = viewModel::confirmSettings,
        onNavigateBack = onNavigateToMainScreen
    )
}