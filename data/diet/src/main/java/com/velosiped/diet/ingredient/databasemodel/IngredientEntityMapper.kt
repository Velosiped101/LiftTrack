package com.velosiped.diet.ingredient.databasemodel

import com.velosiped.diet.ingredient.repository.Ingredient

internal fun IngredientEntity.toIngredient() = Ingredient(
    id = this.id,
    name = this.name,
    protein = this.protein,
    fat = this.fat,
    carbs = this.carbs
)

internal fun Ingredient.toIngredientEntity() = IngredientEntity(
    id = null,
    name = this.name,
    protein = this.protein,
    fat = this.fat,
    carbs = this.carbs
)