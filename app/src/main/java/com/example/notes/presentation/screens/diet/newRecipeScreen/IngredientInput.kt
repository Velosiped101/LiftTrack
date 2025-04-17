package com.example.notes.presentation.screens.diet.newRecipeScreen

import com.example.notes.utils.EMPTY_STRING

data class IngredientInput(
    val name: String = EMPTY_STRING,
    val protein: String = EMPTY_STRING,
    val fat: String = EMPTY_STRING,
    val carbs: String = EMPTY_STRING,
    val mass: String = EMPTY_STRING
) {
    val isValidIngredient: Boolean
        get() = arrayOf(name, protein, fat, carbs, mass).none { it.isBlank() }
}