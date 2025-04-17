package com.example.notes.presentation.screens.diet.addMealScreen

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.notes.data.local.food.Food
import com.example.notes.utils.EMPTY_STRING

data class AddMealUiState(
    val holder: FoodHolder<List<Food>> = FoodHolder.Start(),
    val foodMass: Int? = null,
    val pickedFood: Food? = null,
    val pickedFoodList: SnapshotStateMap<Food, Int> = mutableStateMapOf(),
    val searchBarText: String = EMPTY_STRING,
    val getFromLocal: Boolean = true,
    val getFromRemote: Boolean = false
) {
    val isMassValid: Boolean
        get() = (foodMass != null)
}