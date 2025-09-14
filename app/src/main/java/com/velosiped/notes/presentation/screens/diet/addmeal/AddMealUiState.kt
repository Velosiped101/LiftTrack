package com.velosiped.notes.presentation.screens.diet.addmeal

import androidx.paging.PagingData
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.utils.Constants.EMPTY_STRING
import com.velosiped.notes.utils.NutrientsIntake
import com.velosiped.notes.utils.SearchMode
import kotlinx.coroutines.flow.Flow

data class AddMealUiState(
    val currentIntake: NutrientsIntake = NutrientsIntake(),
    val targetCalories: Int = 0,
    val pagingDataFlow: Flow<PagingData<Food>>? = null,
    val selectedFood: Food? = null,
    val selectedFoodMass: Int? = null,
    val selectedFoodMap: Map<Food, Int> = emptyMap(),
    val searchBarQuery: String = EMPTY_STRING,
    val searchMode: SearchMode = SearchMode.Local
)