package com.velosiped.notes.presentation.screens.diet.addMealScreen

import androidx.paging.PagingData
import com.velosiped.notes.data.api.foodApi.cut
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.utils.EMPTY_STRING
import com.velosiped.notes.utils.SearchMode
import kotlinx.coroutines.flow.Flow

data class AddMealUiState(
    val pagingDataFlow: Flow<PagingData<Food>>? = null,
    val foodMass: Int? = null,
    val pickedFood: Food? = null,
    val pickedFoodList: Map<Food, Int> = mapOf(),
    val searchBarText: String = EMPTY_STRING,
    val searchMode: SearchMode = SearchMode.Local,
    val currentTotalCals: Int = 0,
    val currentTotalProtein: Double = 0.0,
    val currentTotalFat: Double = 0.0,
    val currentTotalCarbs: Double = 0.0,
    val targetCalories: Int = 0
) {
    val isMassValid: Boolean
        get() = (foodMass != null)
    val pickedFoodListTotalProtein = pickedFoodList.entries.sumOf { (food, mass) ->
        food.protein * mass/100
    }.cut()
    val pickedFoodListTotalFat = pickedFoodList.entries.sumOf { (food, mass) ->
        food.fat * mass/100
    }.cut()
    val pickedFoodListTotalCarbs = pickedFoodList.entries.sumOf { (food, mass) ->
        food.carbs * mass/100
    }.cut()
    val pickedFoodTotalCals = (pickedFoodListTotalProtein + pickedFoodListTotalCarbs) * 4 + pickedFoodListTotalFat * 9
}