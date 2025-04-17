package com.example.notes.presentation.screens.diet.newRecipeScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.notes.data.local.food.Ingredient

data class NewRecipeUiState(
    val ingredientsList: SnapshotStateList<IngredientInput> = mutableStateListOf(IngredientInput()),
    val recipeName: String = "Custom recipe",
    val totalMass: Int = 0,
    val ingredientsFoundList: List<Ingredient> = listOf(),
    val recipeImage: String? = null
) {
    val numberOfIngredients: Int
        get() = ingredientsList.size
    val totalMassCalc: Int
        get() = ingredientsList.sumOf { it.mass.toIntOrNull() ?: 0 }
}