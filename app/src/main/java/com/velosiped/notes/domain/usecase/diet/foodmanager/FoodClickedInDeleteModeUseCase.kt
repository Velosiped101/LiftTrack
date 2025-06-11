package com.velosiped.notes.domain.usecase.diet.foodmanager

import com.velosiped.notes.data.database.food.Food

class FoodClickedInDeleteModeUseCase {
    operator fun invoke(food: Food, initialList: List<Food>): List<Food> {
        return initialList.toMutableList().apply {
            if (contains(food)) remove(food) else add(food)
        }
    }
}