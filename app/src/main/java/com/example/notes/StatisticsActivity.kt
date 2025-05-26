package com.example.notes

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import com.example.notes.presentation.navigation.Navigation
import com.example.notes.presentation.navigation.Routes
import com.example.notes.presentation.screens.settingsScreen.SettingsViewModel
import com.example.notes.presentation.screens.statisticsScreen.StatisticsScreen
import com.example.notes.presentation.screens.statisticsScreen.StatisticsViewModel
import com.example.notes.ui.theme.CustomTheme
import com.example.notes.utils.ThemeMode
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