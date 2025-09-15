package com.velosiped.notes.presentation.screens.diet.foodManagerScreen

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
        foodInput = uiState.pickedFoodInput,
        foodList = uiState.foodList,
        inDeleteMode = uiState.inDeleteMode,
        markedForDeleteList = uiState.selectedForDeleteList,
        loadingFinished = viewModel.loadingFinished,
        setInitialFoodInputState = viewModel::setInitialFoodInputState,
        exitDeleteMode = viewModel::exitDeleteMode,
        onNameChange = viewModel::onFoodNameChanged,
        onProteinChange = viewModel::onProteinChanged,
        onFatChange = viewModel::onFatChanged,
        onCarbsChange = viewModel::onCarbsChanged,
        markFoodForDelete = viewModel::markFoodForDelete,
        onNavigateBack = onNavigateBack,
        onDeleteCurrentPhoto = viewModel::setInputImageUriToNull,
        onDeleteMarkedFood = viewModel::deleteMarkedFood,
        onSaveChanges = viewModel::saveChanges,
        onPhotoTaken = viewModel::updateInputImageUri
    )
}