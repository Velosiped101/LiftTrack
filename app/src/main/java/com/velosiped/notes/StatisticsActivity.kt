package com.velosiped.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.velosiped.notes.presentation.screens.settingsScreen.SettingsViewModel
import com.velosiped.notes.presentation.screens.statisticsScreen.StatisticsScreen
import com.velosiped.notes.presentation.screens.statisticsScreen.StatisticsViewModel
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.ThemeMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val settingsUiState by settingsViewModel.uiState.collectAsState()
            val viewModel = hiltViewModel<StatisticsViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            CustomTheme(
                darkTheme = when (settingsUiState.appThemeMode) {
                    ThemeMode.System -> isSystemInDarkTheme()
                    ThemeMode.Dark -> true
                    ThemeMode.Light -> false
                }
            ) {
                StatisticsScreen(uiState = uiState, uiAction = viewModel::uiAction) {
                    finish()
                }
            }
        }
    }
}