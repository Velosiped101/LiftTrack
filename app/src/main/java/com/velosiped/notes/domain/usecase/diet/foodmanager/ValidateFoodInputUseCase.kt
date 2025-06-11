package com.velosiped.notes.domain.usecase.diet.foodmanager

import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.FoodInput

class ValidateFoodInputUseCase {
    operator fun invoke(foodInput: FoodInput): Boolean {
        return (foodInput.name.isNotBlank()) &&
                (foodInput.protein.toDoubleOrNull() != null) &&
                (foodInput.fat.toDoubleOrNull() != null) &&
                (foodInput.carbs.toDoubleOrNull() != null)
    }
}