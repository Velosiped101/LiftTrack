package com.velosiped.addmeal.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun AddMealScreenWrapper(
    viewModel: AddMealViewModel,
    navigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    val saveCompleted = viewModel.saveCompleted

    LaunchedEffect(Unit) {
        saveCompleted.collect {
            navigateBack()
        }
    }

    AddMealScreen(
        pagingDataFlow = uiState.pagingDataFlow,
        searchBarState = uiState.searchBarState,
        searchMode = uiState.searchMode,
        currentIntake = uiState.currentIntake,
        selectedFoodIntake = uiState.selectedFoodIntake,
        selectedFoodMap = uiState.selectedFoodMap,
        targetCalories = uiState.targetCalories,
        massInputState = uiState.massInputState,
        onFoodClick = viewModel::onFoodPicked,
        onDeleteFromSelectedMap = viewModel::onDeleteFromSelectedMap,
        onConfirmAddingToSelectedMap = viewModel::onConfirmAddingToSelectedMap,
        onSearchInputChange = viewModel::onSearchInputChange,
        onSearchModeChange = viewModel::onSearchModeChange,
        onSearchInputClear = viewModel::onSearchInputClear,
        onMassInputChange = viewModel::onMassInputChange,
        onConfirmMeal = viewModel::onConfirmMeal,
        onNavigateBack = { navigateBack() }
    )
}