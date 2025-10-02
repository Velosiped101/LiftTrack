package com.velosiped.notes.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.velosiped.notes.utils.IntentExtras
import com.velosiped.statistic.presentation.StatisticsScreenWrapper
import com.velosiped.statistic.presentation.StatisticsViewModel
import com.velosiped.ui.theme.CustomTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<StatisticsViewModel>()
            CustomTheme(
                darkTheme = intent.getBooleanExtra(IntentExtras.INTENT_EXTRA_DARK_THEME, false)
            ) {
                StatisticsScreenWrapper(
                    viewModel = viewModel,
                    onNavigateBack = { finish() }
                )
            }
        }
    }
}