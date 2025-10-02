package com.velosiped.diet.mealhistory.databasemodel

import com.velosiped.diet.mealhistory.repository.MealHistory

internal fun MealHistoryEntity.toMealHistory() = MealHistory(
    id = this.id,
    time = this.time,
    name = this.name,
    protein = this.protein,
    fat = this.fat,
    carbs = this.carbs,
    mass = this.mass
)

internal fun MealHistory.toMealHistoryEntity() = MealHistoryEntity(
    id = this.id,
    time = this.time,
    name = this.name,
    protein = this.protein,
    fat = this.fat,
    carbs = this.carbs,
    mass = this.mass
)