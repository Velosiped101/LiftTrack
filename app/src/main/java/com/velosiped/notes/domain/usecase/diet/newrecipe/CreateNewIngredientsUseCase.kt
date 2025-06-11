package com.velosiped.notes.domain.usecase.diet.newrecipe

import com.velosiped.notes.data.database.ingredient.Ingredient
import com.velosiped.notes.domain.repository.DietRepository
import com.velosiped.notes.presentation.screens.diet.newRecipeScreen.NewRecipeUiState
import com.velosiped.notes.utils.Constants
import javax.inject.Inject

class CreateNewIngredientsUseCase @Inject constructor(
    private val dietRepository: DietRepository
) {
    suspend operator fun invoke(uiState: NewRecipeUiState) {
        uiState.ingredientsList.forEach {
            dietRepository.insertIngredient(
                Ingredient(
                    id = if (it.id < Constants.GENERATED_ID_INITIAL) it.id else null,
                    name = it.name,
                    protein = it.protein.toDouble(),
                    fat = it.fat.toDouble(),
                    carbs = it.carbs.toDouble()
                )
            )
        }
    }
}