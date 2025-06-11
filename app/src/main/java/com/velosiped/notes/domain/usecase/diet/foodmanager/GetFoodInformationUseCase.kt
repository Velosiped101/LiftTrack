package com.velosiped.notes.domain.usecase.diet.foodmanager

import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.FoodInput
import com.velosiped.notes.utils.toFoodInput

class GetFoodInformationUseCase {
    operator fun invoke(food: Food?): FoodInput {
        return food.toFoodInput()
    }
}