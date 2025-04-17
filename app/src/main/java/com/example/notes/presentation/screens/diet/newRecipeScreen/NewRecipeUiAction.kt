package com.example.notes.presentation.screens.diet.newRecipeScreen

import android.content.Context
import com.example.notes.data.local.food.Ingredient

sealed interface NewRecipeUiAction {
    data object IncreaseNumberOfIngredients: NewRecipeUiAction
    data object DecreaseNumberOfIngredients: NewRecipeUiAction
    data object ConfirmNewFood: NewRecipeUiAction
    data object ConfirmFoodOptions: NewRecipeUiAction
    data class OnIngredientNameChanged(val id: Int, val text: String): NewRecipeUiAction
    data class OnIngredientProteinChanged(val id: Int, val text: String): NewRecipeUiAction
    data class OnIngredientFatChanged(val id: Int, val text: String): NewRecipeUiAction
    data class OnIngredientCarbsChanged(val id: Int, val text: String): NewRecipeUiAction
    data class OnIngredientMassChanged(val id: Int, val text: String): NewRecipeUiAction
    data class OnRecipeNameChanged(val text: String): NewRecipeUiAction
    data class OnRecipeMassChanged(val text: String): NewRecipeUiAction
    data class DeleteIngredient(val id: Int): NewRecipeUiAction
    data class FillIngredientInfo(val id: Int, val ingredient: Ingredient): NewRecipeUiAction
}