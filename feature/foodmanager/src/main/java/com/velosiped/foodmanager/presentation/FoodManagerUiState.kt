package com.velosiped.foodmanager.presentation

import android.net.Uri
import com.velosiped.diet.food.repository.Food
import com.velosiped.foodmanager.presentation.utils.FoodInputState

data class FoodManagerUiState(
    val foodList: List<Food> = emptyList(),
    val markedForDeleteList: List<Food> = listOf(),
    val foodInputState: FoodInputState = FoodInputState(),
    val tempFileUri: Uri? = null
)