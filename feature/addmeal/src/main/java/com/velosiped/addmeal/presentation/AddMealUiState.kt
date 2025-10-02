package com.velosiped.addmeal.presentation

import androidx.paging.PagingData
import com.velosiped.addmeal.presentation.utils.SearchMode
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.diet.food.repository.Food
import com.velosiped.utility.data.NutrientsIntake
import com.velosiped.utility.extensions.ZERO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class AddMealUiState(
    val currentIntake: NutrientsIntake = NutrientsIntake(),
    val selectedFoodIntake: NutrientsIntake = NutrientsIntake(),
    val targetCalories: Int = Int.ZERO,
    val pagingDataFlow: Flow<PagingData<Food>> = emptyFlow(),
    val selectedFood: Food? = null,
    val selectedFoodMap: Map<Food, Int> = emptyMap(),
    val searchMode: SearchMode = SearchMode.Local,
    val searchBarState: TextFieldState = TextFieldState(),
    val massInputState: TextFieldState = TextFieldState()
)