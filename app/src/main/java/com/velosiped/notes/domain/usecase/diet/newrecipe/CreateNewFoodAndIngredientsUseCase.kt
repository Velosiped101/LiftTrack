package com.velosiped.notes.domain.usecase.diet.newrecipe

import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.domain.repository.DietRepository
import com.velosiped.notes.presentation.screens.diet.newRecipeScreen.NewRecipeUiState
import com.velosiped.notes.utils.cut
import javax.inject.Inject

class CreateNewFoodUseCase @Inject constructor(
    private val dietRepository: DietRepository
) {
    suspend operator fun invoke(uiState: NewRecipeUiState) {
        val totalMass = uiState.let {
            if (it.useAutoMass) it.autoCalculatedTotalMass
            else it.userDefinedTotalMass.toIntOrNull() ?: 0
        }.takeIf { it != 0 } ?: return

        val ingredientsList = uiState.ingredientsList

        val name = uiState.recipeName
        val protein = (ingredientsList.sumOf { it.protein.toDouble() * it.mass.toInt() / 100.0 } / (totalMass / 100.0)).cut()
        val fat = (ingredientsList.sumOf { it.fat.toDouble() * it.mass.toInt() / 100.0 } / (totalMass / 100.0)).cut()
        val carbs = (ingredientsList.sumOf { it.carbs.toDouble() * it.mass.toInt() / 100.0 } / (totalMass / 100.0)).cut()
        val imageUri = uiState.imageUri?.toString()

        dietRepository.insert(
            Food(
                name = name,
                protein = protein,
                fat = fat,
                carbs = carbs,
                imageUrl = imageUri
            )
        )
    }
}