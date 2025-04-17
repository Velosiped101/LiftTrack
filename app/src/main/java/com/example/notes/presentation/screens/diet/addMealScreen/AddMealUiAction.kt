package com.example.notes.presentation.screens.diet.addMealScreen

import com.example.notes.data.local.food.Food

sealed interface AddMealUiAction {
    data object ResetUi : AddMealUiAction
    data class Search(val name: String) : AddMealUiAction
    data object ConfirmMeal : AddMealUiAction
    data class PickFood(val food: Food) : AddMealUiAction
    data object AddFoodToPickedList : AddMealUiAction
    data class RemoveFromPickedList(val food: Food) : AddMealUiAction
    data object SearchInLocal : AddMealUiAction
    data object SearchInRemote : AddMealUiAction
    data class OnMassInputChanged(val mass: String) : AddMealUiAction
}