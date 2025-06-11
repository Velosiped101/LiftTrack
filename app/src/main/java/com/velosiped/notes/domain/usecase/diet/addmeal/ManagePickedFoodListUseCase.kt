package com.velosiped.notes.domain.usecase.diet.addmeal

import com.velosiped.notes.data.database.food.Food

class ManagePickedFoodListUseCase {
    fun addFood(food: Food, mass: Int, initialMap: Map<Food, Int>): Map<Food, Int> =
        initialMap.toMutableMap().apply { put(food, mass) }

    fun deleteFood(food: Food, initialMap: Map<Food, Int>): Map<Food, Int> =
        initialMap.toMutableMap().apply { remove(food) }

    fun getFood(food: Food, initialMap: Map<Food, Int>): Pair<Food, Int?> =
        if (initialMap.containsKey(food)) Pair(food, initialMap[food]) else Pair(food, null)
}