package com.velosiped.diet.ingredient.repository

data class Ingredient(
    val id: Long? = null,
    val name: String,
    val protein: Float,
    val fat: Float,
    val carbs: Float
)