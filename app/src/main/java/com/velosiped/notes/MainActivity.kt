package com.velosiped.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.velosiped.notes.presentation.navigation.Navigation
import com.velosiped.notes.presentation.screens.main.MainViewModel
import com.velosiped.notes.presentation.screens.settingsScreen.SettingsViewModel
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.ThemeMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val mainViewModel = hiltViewModel<MainViewModel>()
            val settingsUiState by settingsViewModel.uiState.collectAsState()
            CustomTheme(
                darkTheme = when (settingsUiState.appThemeMode) {
                    ThemeMode.System -> isSystemInDarkTheme()
                    ThemeMode.Dark -> true
                    ThemeMode.Light -> false
                }
            ) {
                Navigation(
                    settingsViewModel = settingsViewModel,
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
        }
    }
}