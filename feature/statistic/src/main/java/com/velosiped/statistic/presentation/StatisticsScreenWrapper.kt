package com.velosiped.statistic.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun StatisticsScreenWrapper(
    viewModel: StatisticsViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    StatisticsScreen(
        exerciseList = uiState.exercises,
        formula = uiState.currentFormula,
        data = uiState.data,
        onFormulaChange = viewModel::setFormula,
        onExerciseChange = viewModel::setExercise,
        onNavigateBack = onNavigateBack
    )
}