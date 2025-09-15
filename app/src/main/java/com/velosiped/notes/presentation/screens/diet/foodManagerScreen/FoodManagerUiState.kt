package com.velosiped.notes.presentation.screens.diet.foodManagerScreen

import android.net.Uri
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.utils.EMPTY

data class FoodManagerUiState(
    val foodList: List<Food> = listOf(),
    val inDeleteMode: Boolean = false,
    val pickedFood: Food? = null,
    val pickedFoodInput: FoodInput = FoodInput(),
    val selectedForDeleteList: List<Food> = listOf(),
    val tempFileUri: Uri? = null
) {
    data class FoodInput(
        val id: Int? = null,
        val name: String = String.EMPTY,
        val protein: String = String.EMPTY,
        val fat: String = String.EMPTY,
        val carbs: String = String.EMPTY,
        val imageUri: Uri? = null
    ) {
        val allParametersAreFilled: Boolean
            get() = listOf(name, protein, fat, carbs).none { it.isBlank() }
    }
}