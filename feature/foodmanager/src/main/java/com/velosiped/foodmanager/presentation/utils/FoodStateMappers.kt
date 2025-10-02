package com.velosiped.foodmanager.presentation.utils

import com.velosiped.diet.food.repository.Food
import com.velosiped.ui.model.textfieldvalidator.TextFieldState

fun Food.toFoodInputState() = FoodInputState(
    id = this.id,
    imageUri = this.imageUri,
    nameFieldState = TextFieldState(text = this.name),
    proteinFieldState = TextFieldState(text = this.protein.toString()),
    fatFieldState = TextFieldState(text = this.fat.toString()),
    carbsFieldState = TextFieldState(text = this.carbs.toString())
)

fun FoodInputState.toFood() = Food(
    id = this.id,
    name = this.nameFieldState.text,
    protein = this.proteinFieldState.text.toFloat(),
    fat = this.fatFieldState.text.toFloat(),
    carbs = this.carbsFieldState.text.toFloat(),
    imageUri = this.imageUri
)