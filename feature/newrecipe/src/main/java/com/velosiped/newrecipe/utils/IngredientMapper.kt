package com.velosiped.newrecipe.utils

import com.velosiped.diet.ingredient.repository.Ingredient
import com.velosiped.newrecipe.presentation.components.utils.IngredientInputState
import com.velosiped.ui.model.textfieldvalidator.TextFieldState

fun Ingredient.toIngredientInputState() = IngredientInputState(
    id = this.id,
    nameFieldState = TextFieldState(text = this.name),
    proteinFieldState = TextFieldState(text = this.protein.toString()),
    fatFieldState = TextFieldState(text = this.fat.toString()),
    carbsFieldState = TextFieldState(text = this.carbs.toString()),
    readOnly = true
)

fun IngredientInputState.toIngredient() = Ingredient(
    id = this.id,
    name = this.nameFieldState.text,
    protein = this.proteinFieldState.text.toFloat(),
    fat = this.fatFieldState.text.toFloat(),
    carbs = this.carbsFieldState.text.toFloat()
)