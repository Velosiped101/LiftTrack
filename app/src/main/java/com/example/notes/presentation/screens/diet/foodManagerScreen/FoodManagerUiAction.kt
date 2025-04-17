package com.example.notes.presentation.screens.diet.foodManagerScreen

import android.content.Context
import android.net.Uri
import com.example.notes.data.local.food.Food

sealed interface FoodManagerUiAction {
    data object ExitDeleteMode: FoodManagerUiAction
    data class GetFoodInformation(val food: Food?): FoodManagerUiAction
    data class DeleteFood(val context: Context): FoodManagerUiAction
    data class OnFoodClick(val food: Food): FoodManagerUiAction
    data class OnFoodLongClick(val food: Food): FoodManagerUiAction
    data object OnConfirmDialog: FoodManagerUiAction
    data class OnFoodNameChanged(val name: String): FoodManagerUiAction
    data class OnFoodProteinChanged(val protein: String): FoodManagerUiAction
    data class OnFoodFatChanged(val fat: String): FoodManagerUiAction
    data class OnFoodCarbsChanged(val carbs: String): FoodManagerUiAction
    data object DeleteFoodImage: FoodManagerUiAction
    data class CreateImageFile(val context: Context): FoodManagerUiAction
    data class DeleteImageFile(val context: Context, val uri: Uri): FoodManagerUiAction
    data object UpdateImage: FoodManagerUiAction
}