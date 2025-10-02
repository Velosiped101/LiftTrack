package com.velosiped.settings.presentation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.settings.R
import com.velosiped.settings.presentation.components.BodyParametersSetter
import com.velosiped.settings.presentation.components.ResetTimeSetter
import com.velosiped.settings.presentation.components.SettingsSegmentedButton
import com.velosiped.settings.presentation.components.SettingsSegmentedButtonOption
import com.velosiped.settings.presentation.components.TargetCaloriesCard
import com.velosiped.settings.presentation.utils.DailyActivity
import com.velosiped.settings.presentation.utils.Goal
import com.velosiped.settings.presentation.utils.ResetTime
import com.velosiped.settings.presentation.utils.Sex
import com.velosiped.ui.utils.ThemeMode
import com.velosiped.ui.components.CustomOutlinedButton
import com.velosiped.ui.components.topbar.BaseTopBar
import com.velosiped.ui.components.topbar.TopBarHeader
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.ui.R as coreR

@Composable
fun SettingsScreen(
    isNotFirstLaunch: Boolean?,
    currentTheme: ThemeMode,
    resetTime: ResetTime,
    sex: Sex,
    age: Int,
    height: Int,
    bodyMass: Float,
    dailyActivity: DailyActivity,
    goal: Goal,
    autoTargetCalories: Int,
    onRestoreState: () -> Unit,
    onThemeChange: (ThemeMode) -> Unit,
    onResetTimeChange: (Int, Int) -> Unit,
    onSexChange: (Sex) -> Unit,
    onAgeChange: (Int) -> Unit,
    onHeightChange: (Int) -> Unit,
    onBodyMassChange: (Float) -> Unit,
    onDailyActivityChange: (DailyActivity) -> Unit,
    onGoalChange: (Goal) -> Unit,
    onConfirm: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val activity = LocalContext.current as? Activity
    fun handleBack() {
        if (isNotFirstLaunch == true) {
            onNavigateBack()
            onRestoreState()
        } else {
            activity?.finish()
        }
    }
    CustomTheme(
        darkTheme = when (currentTheme) {
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
        }
    ) {
        BackHandler { handleBack() }
        Scaffold(
            topBar = {
                BaseTopBar(
                    onNavigateBack = { handleBack() },
                    header = { TopBarHeader(text = stringResource(R.string.settings_headline)) }
                )
            },
            containerColor = CustomTheme.colors.mainBackgroundColor
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(innerPadding)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_8)),
                    modifier = Modifier
                        .weight(Float.ONE)
                        .padding(dimensionResource(coreR.dimen.space_by_8))
                        .verticalScroll(rememberScrollState())
                ) {
                    SettingsSegmentedButton(
                        onValueSelected = onThemeChange,
                        currentValue = currentTheme,
                        values = ThemeMode.entries,
                        headerText = stringResource(R.string.theme),
                        valueContent = { SettingsSegmentedButtonOption(text = stringResource(it.textId)) }
                    )
                    ResetTimeSetter(
                        time = resetTime,
                        onTimeChange = onResetTimeChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SettingsSegmentedButton(
                        onValueSelected = onSexChange,
                        currentValue = sex,
                        values = Sex.entries,
                        headerText = stringResource(R.string.sex),
                        valueContent = { SettingsSegmentedButtonOption(text = stringResource(it.textId)) }
                    )
                    BodyParametersSetter(
                        currentBodyMass = bodyMass,
                        currentAge = age,
                        currentHeight = height,
                        onBodyMassChange = onBodyMassChange,
                        onAgeChange = onAgeChange,
                        onHeightChange = onHeightChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SettingsSegmentedButton(
                        onValueSelected = onDailyActivityChange,
                        currentValue = dailyActivity,
                        values = DailyActivity.entries,
                        headerText = stringResource(R.string.daily_activity),
                        valueContent = { SettingsSegmentedButtonOption(text = stringResource(it.textId)) }
                    )
                    SettingsSegmentedButton(
                        onValueSelected = onGoalChange,
                        currentValue = goal,
                        values = Goal.entries,
                        headerText = stringResource(R.string.goal),
                        valueContent = { SettingsSegmentedButtonOption(text = stringResource(it.textId)) }
                    )
                    TargetCaloriesCard(targetCalories = autoTargetCalories)
                }
                CustomOutlinedButton(
                    onClick = { onConfirm() },
                    text = stringResource(id = R.string.settings_confirm),
                    modifier = Modifier.padding(dimensionResource(coreR.dimen.space_by_4))
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        SettingsScreen(
            isNotFirstLaunch = true,
            currentTheme = ThemeMode.LIGHT,
            resetTime = ResetTime(20, 30),
            sex = Sex.MALE,
            age = 20,
            height = 170,
            bodyMass = 65f,
            dailyActivity = DailyActivity.SLIGHT,
            goal = Goal.GAIN,
            autoTargetCalories = 2500,
            onRestoreState = {  },
            onThemeChange = {  },
            onResetTimeChange = { _, _ -> },
            onSexChange = {  },
            onAgeChange = {  },
            onHeightChange = {  },
            onBodyMassChange = {  },
            onDailyActivityChange = {  },
            onGoalChange = {  },
            onConfirm = {  },
            onNavigateBack = {  }
        )
    }
}