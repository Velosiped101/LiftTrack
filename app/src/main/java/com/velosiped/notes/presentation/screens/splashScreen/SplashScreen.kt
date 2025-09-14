package com.velosiped.notes.presentation.screens.splashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.settingsScreen.SettingsViewModel
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun SplashScreen(
    viewModel: SettingsViewModel,
    onNavigateToMain: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val isNotFirstLaunch = viewModel.uiState.collectAsState().value.isNotFirstLaunch
    val isSuperScreen = (0 .. 500).random() in (13 .. 37)

    LaunchedEffect(key1 = isNotFirstLaunch) {
        isNotFirstLaunch?.let {
            if (isNotFirstLaunch) {
                onNavigateToMain()
            } else {
                onNavigateToSettings()
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background(CustomTheme.colors.mainBackgroundColor)
    ) {
        if (isSuperScreen) Image(
            painter = painterResource(id = R.drawable.smile),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(.25f)
        ) else Icon(
            painter = painterResource(id = R.drawable.hourglass),
            contentDescription = null,
            tint = CustomTheme.colors.iconsTintColor,
            modifier = Modifier
                .fillMaxWidth(.25f).scale(.5f)
        )
    }
}