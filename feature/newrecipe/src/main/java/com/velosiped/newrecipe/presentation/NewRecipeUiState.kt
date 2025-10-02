package com.velosiped.newrecipe.presentation

import android.net.Uri
import com.velosiped.diet.ingredient.repository.Ingredient
import com.velosiped.newrecipe.presentation.components.utils.IngredientInputState
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.utility.extensions.ZERO

data class NewRecipeUiState(
    val tempFileUri: Uri? = null,
    val currentImageUri: Uri? = null,
    val ingredients: List<IngredientInputState> = listOf(IngredientInputState()),
    val ingredientsFound: List<Ingredient> = emptyList(),
    val recipeNameState: TextFieldState = TextFieldState(),
    val recipeMassState: TextFieldState = TextFieldState(),
    val useAutoMass: Boolean = false
) {
    val autoCalculatedRecipeMass: Int
        get() = ingredients.sumOf { it.massFieldState.text.toIntOrNull() ?: Int.ZERO }
}