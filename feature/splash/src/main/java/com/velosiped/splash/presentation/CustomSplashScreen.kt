package com.velosiped.splash.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF
import com.velosiped.utility.extensions.QUARTER

@Composable
fun CustomSplashScreen(
    isNotFirstLaunch: Boolean?,
    onNavigateToMain: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
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
        modifier = Modifier
            .fillMaxSize()
            .background(CustomTheme.colors.mainBackgroundColor)
    ) {
        if (isSuperScreen) {
            Image(
                painter = painterResource(id = R.drawable.smile),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(Float.QUARTER)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.hourglass),
                contentDescription = null,
                tint = CustomTheme.colors.iconsTintColor,
                modifier = Modifier
                    .fillMaxWidth(Float.QUARTER)
                    .scale(Float.HALF)
            )
        }
    }
}