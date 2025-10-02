package com.velosiped.newrecipe.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun NewRecipeScreenWrapper(
    viewModel: NewRecipeViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.saveCompleteFlow.collect {
            onNavigateBack()
        }
    }

    NewRecipeScreen(
        ingredients = uiState.ingredients,
        ingredientsFound = uiState.ingredientsFound,
        recipeNameFieldState = uiState.recipeNameState,
        tempFileUri = uiState.tempFileUri,
        currentImageUri = uiState.currentImageUri,
        massFieldInputState = uiState.recipeMassState,
        useAutoMass = uiState.useAutoMass,
        onFocusChanged = viewModel::onFocusChange,
        onPhotoTaken = viewModel::onPhotoTaken,
        onRecipeNameChange = viewModel::onRecipeNameChange,
        onAutoMassCalcChange = viewModel::onMassSourceChange,
        onDeleteCurrentPhoto = viewModel::onDeleteCurrentPhoto,
        onRecipeMassChange = viewModel::onRecipeMassChange,
        onDeleteIngredient = viewModel::onDeleteIngredient,
        onAutoCompleteIngredientInput = viewModel::onAutoCompleteIngredientInput,
        onNameChange = viewModel::onIngredientNameChange,
        onProteinChange = viewModel::onIngredientProteinChange,
        onFatChange = viewModel::onIngredientFatChange,
        onCarbsChange = viewModel::onIngredientCarbsChange,
        onMassChange = viewModel::onIngredientMassChange,
        onDecrease = viewModel::onDecreaseNumberOfIngredients,
        onIncrease = viewModel::onIncreaseNumberOfIngredients,
        onConfirm = viewModel::onConfirmNewFood,
        onNavigateBack = onNavigateBack
    )
}