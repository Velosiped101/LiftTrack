package com.velosiped.notes.presentation.screens.diet.addMealScreen

import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.utils.SearchMode

sealed interface AddMealUiAction {
    data class SearchBarTextChanged(val name: String) : AddMealUiAction
    data object ConfirmMeal : AddMealUiAction
    data class PickFood(val food: Food) : AddMealUiAction
    data object AddFoodToPickedList : AddMealUiAction
    data class RemoveFromPickedList(val food: Food) : AddMealUiAction
    data class SetSearchMode(val searchMode: SearchMode) : AddMealUiAction
    data class OnMassInputChanged(val mass: String) : AddMealUiAction
    data object ClearSearchBarInput: AddMealUiAction
}