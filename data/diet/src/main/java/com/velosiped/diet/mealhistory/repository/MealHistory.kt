package com.velosiped.diet.mealhistory.repository

data class MealHistory(
    val id: Int? = null,
    val time: String,
    val name: String,
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    val mass: Int
)