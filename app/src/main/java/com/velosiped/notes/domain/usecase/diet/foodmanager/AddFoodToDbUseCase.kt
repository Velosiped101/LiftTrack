package com.velosiped.notes.domain.usecase.diet.foodmanager

import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.domain.repository.DietRepository
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.FoodManagerUiState.FoodInput
import com.velosiped.notes.utils.toFood
import javax.inject.Inject

class AddFoodToDbUseCase @Inject constructor(
    private val dietRepository: DietRepository
) {
    suspend operator fun invoke(food: Food) {
        dietRepository.insert(food)
    }
}