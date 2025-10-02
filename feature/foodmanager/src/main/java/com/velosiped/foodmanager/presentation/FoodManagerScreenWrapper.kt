package com.velosiped.foodmanager.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun FoodManagerScreenWrapper(
    viewModel: FoodManagerViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value

    FoodManagerScreen(
        tempFileUri = uiState.tempFileUri,
        foodInputState = uiState.foodInputState,
        foodList = uiState.foodList,
        markedForDeleteList = uiState.markedForDeleteList,
        loadingFinished = viewModel.loadingFinished,
        onFoodClick = viewModel::onFoodClick,
        onExitDeleteMode = viewModel::onExitDeleteMode,
        onNameChange = viewModel::onNameChange,
        onProteinChange = viewModel::onProteinChange,
        onFatChange = viewModel::onFatChange,
        onCarbsChange = viewModel::onCarbsChange,
        onFoodLongClick = viewModel::onFoodLongClick,
        onDeleteCurrentPhoto = viewModel::onDeleteCurrentPhoto,
        onDeleteMarkedFood = viewModel::onDeleteMarkedFood,
        onSaveChanges = viewModel::onSaveChanges,
        onPhotoTaken = viewModel::onPhotoTaken,
        onNavigateBack = onNavigateBack
    )
}