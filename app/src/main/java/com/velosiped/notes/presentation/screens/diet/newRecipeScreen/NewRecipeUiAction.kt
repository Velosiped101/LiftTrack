package com.velosiped.notes.presentation.screens.diet.newRecipeScreen

import android.content.Context
import com.velosiped.notes.data.database.ingredient.Ingredient

sealed interface NewRecipeUiAction {
    data object IncreaseNumberOfIngredients: NewRecipeUiAction
    data object DecreaseNumberOfIngredients: NewRecipeUiAction
    data object ConfirmNewFood: NewRecipeUiAction
    data class OnIngredientNameChanged(val ingredient: IngredientInput, val input: String): NewRecipeUiAction
    data class OnIngredientProteinChanged(val ingredient: IngredientInput, val input: String): NewRecipeUiAction
    data class OnIngredientFatChanged(val ingredient: IngredientInput, val input: String): NewRecipeUiAction
    data class OnIngredientCarbsChanged(val ingredient: IngredientInput, val input: String): NewRecipeUiAction
    data class OnIngredientMassChanged(val ingredient: IngredientInput, val input: String): NewRecipeUiAction
    data class OnRecipeNameChanged(val input: String): NewRecipeUiAction
    data class OnRecipeMassChanged(val input: String): NewRecipeUiAction
    data class DeleteIngredient(val ingredient: IngredientInput): NewRecipeUiAction
    data class FillIngredientInfo(val ingredientInput: IngredientInput, val ingredient: Ingredient): NewRecipeUiAction
    data object DeleteFoodImage: NewRecipeUiAction
    data class CreateImageFile(val context: Context): NewRecipeUiAction
    data class DeleteImageFile(val context: Context, val uri: String?): NewRecipeUiAction
    data object UpdateImage: NewRecipeUiAction
    data class ChangeMassSource(val useAutoMass: Boolean): NewRecipeUiAction
}