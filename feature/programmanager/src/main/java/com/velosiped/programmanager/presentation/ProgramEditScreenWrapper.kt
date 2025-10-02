package com.velosiped.programmanager.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun ProgramEditScreenWrapper(
    viewModel: ProgramEditViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    ProgramEditScreen(
        exerciseList = uiState.exerciseList,
        programList = uiState.programList,
        programItemState = uiState.programItemState,
        onDaySelect = viewModel::onDayChange,
        onProgramItemClick = viewModel::onProgramItemClick,
        onExerciseClick = viewModel::onExerciseClick,
        onDeleteFromProgram = viewModel::onDeleteFromProgram,
        onAddToProgram = viewModel::onAddToProgram,
        onAddNewClick = viewModel::onAddNewClick,
        onSetsChange = viewModel::onSetsChange,
        onRepsChange = viewModel::onRepsChange,
        onDropProgramForTheDay = viewModel::onDropProgramForTheDay,
        onDropProgram = viewModel::onDropProgram,
        onNavigateBack = onNavigateBack
    )
}