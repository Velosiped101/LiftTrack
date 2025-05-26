package com.example.notes.presentation.screens.splashScreen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.example.notes.R
import com.example.notes.presentation.screens.settingsScreen.SettingsViewModel
import kotlinx.coroutines.delay

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
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
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
            tint = MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier
                .fillMaxWidth(.25f).scale(.5f)
        )
    }
}