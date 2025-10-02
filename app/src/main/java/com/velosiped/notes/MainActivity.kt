package com.velosiped.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.velosiped.home.presentation.HomeViewModel
import com.velosiped.notes.navigation.Navigation
import com.velosiped.settings.presentation.SettingsViewModel
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.utils.ThemeMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val homeViewModel = hiltViewModel<HomeViewModel>()
            val themeMode = settingsViewModel.themeMode.collectAsState().value
            val isNotFirstLaunch = settingsViewModel.isNotFirstLaunch.collectAsState().value
            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
            }
            CustomTheme(
                darkTheme = darkTheme
            ) {
                Navigation(
                    settingsViewModel = settingsViewModel,
                    homeViewModel = homeViewModel,
                    isNotFirstLaunch = isNotFirstLaunch,
                    isDarkTheme = darkTheme,
                    navController = navController
                )
            }
        }
    }
}