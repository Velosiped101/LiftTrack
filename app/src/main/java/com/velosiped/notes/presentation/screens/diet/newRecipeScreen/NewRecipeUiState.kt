package com.velosiped.notes.presentation.screens.diet.newRecipeScreen

import android.net.Uri
import com.velosiped.notes.data.database.ingredient.Ingredient
import com.velosiped.notes.utils.Constants

data class NewRecipeUiState(
    val ingredientsList: List<IngredientInput> = listOf(IngredientInput()),
    val ingredientsFoundList: List<Ingredient> = listOf(),
    val recipeName: String = Constants.EMPTY_STRING,
    val userDefinedTotalMass: String = Constants.EMPTY_STRING,
    val useAutoMass: Boolean = true,
    val imageUri: Uri? = null,
    val generatedUri: Uri? = null,
) {
    val numberOfIngredients: Int
        get() = ingredientsList.size
    val autoCalculatedTotalMass: Int
        get() = ingredientsList.sumOf { it.mass.toIntOrNull() ?: 0 }
    val isValidFood: Boolean
        get() = if (useAutoMass)
            ingredientsList.all { it.isValidIngredient } && autoCalculatedTotalMass != 0
        else
            ingredientsList.all { it.isValidIngredient } && userDefinedTotalMass.isNotBlank()
}

data class IngredientInput(
    val id: Int = Constants.GENERATED_ID_INITIAL,
    val name: String = Constants.EMPTY_STRING,
    val protein: String = Constants.EMPTY_STRING,
    val fat: String = Constants.EMPTY_STRING,
    val carbs: String = Constants.EMPTY_STRING,
    val mass: String = Constants.EMPTY_STRING,
    val readOnly: Boolean = false
) {
    val isValidIngredient: Boolean
        get() = arrayOf(name, protein, fat, carbs, mass).none { it.isBlank() }
    val isEmptyIngredient: Boolean
        get() = arrayOf(name, protein, fat, carbs, mass).all { it.isBlank() }
}