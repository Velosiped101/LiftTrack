package com.example.notes.presentation.screens.diet.foodManagerScreen

import com.example.notes.utils.EMPTY_STRING

data class FoodInput(
    val name: String = EMPTY_STRING,
    val protein: String = EMPTY_STRING,
    val fat: String = EMPTY_STRING,
    val carbs: String = EMPTY_STRING,
    val imageUri: String? = null
)