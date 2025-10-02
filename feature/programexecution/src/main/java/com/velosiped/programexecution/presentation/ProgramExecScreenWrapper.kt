package com.velosiped.programexecution.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ProgramExecScreenWrapper(
    viewModel: ProgramExecViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = Unit) {
        viewModel.saveCompleted.collect {
            onNavigateBack()
        }
    }

    if (uiState.initializationIsComplete) {
        ProgramExecScreen(
            trainingHistory = uiState.trainingHistory,
            showWeightIncreaseHintList = uiState.showHintList,
            initialWeightList = uiState.initialWeight,
            onUpdateStoredProgress = viewModel::onUpdateStoredProgress,
            onConfirm = viewModel::onConfirm,
            onRepsChange = viewModel::onRepsChange,
            onWeightChange = viewModel::onWeightChange,
            onNavigateBack = onNavigateBack
        )
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
}