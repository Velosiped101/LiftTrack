package com.velosiped.diet.food.databasemodel

import androidx.core.net.toUri
import com.velosiped.diet.food.repository.Food

internal fun FoodEntity.toFood() = Food(
    id = this.id,
    name = this.name,
    protein = this.protein,
    fat = this.fat,
    carbs = this.carbs,
    imageUri = this.imageUrl?.toUri()
)

internal fun Food.toFoodEntity() = FoodEntity(
    id = this.id,
    name = this.name,
    protein = this.protein,
    fat = this.fat,
    carbs = this.carbs,
    imageUrl = this.imageUri?.toString()
)