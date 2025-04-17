package com.example.notes.presentation.screens.mainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.presentation.screens.mainScreen.components.DietCard
import com.example.notes.presentation.screens.mainScreen.components.ProgramCard

@Composable
fun MainScreen(
    uiState: MainScreenUiState,
    uiAction: (MainScreenUiAction) -> Unit,
    navigateToNewRecipe: () -> Unit,
    navigateToAddMeal: () -> Unit,
    navigateToFoodDbManager: () -> Unit,
    navigateToProgramManager: () -> Unit,
    navigateToProgramExec: () -> Unit
) {
    Column {
        DietCard(
            uiState = uiState,
            uiAction = uiAction,
            navigateToNewRecipe = navigateToNewRecipe,
            navigateToAddMeal = navigateToAddMeal,
            navigateToFoodDbManager = navigateToFoodDbManager
        )
        ProgramCard(
            uiState = uiState,
            uiAction = uiAction,
            navigateToProgramManager = navigateToProgramManager,
            navigateToProgramExec = navigateToProgramExec
        )
    }
}