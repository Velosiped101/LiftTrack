package com.velosiped.notes.presentation.screens.diet.foodManagerScreen

import android.net.Uri
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.utils.Constants

data class FoodManagerUiState(
    val foodList: List<Food> = listOf(),
    val isInDeleteMode: Boolean = false,
    val pickedFood: Food? = null,
    val pickedFoodInput: FoodInput = FoodInput(),
    val selectedForDeleteList: List<Food> = listOf(),
    val generatedUri: Uri? = null
) {
    val isFoodInputValid: Boolean
        get() = (pickedFoodInput.name.isNotBlank()) &&
                (pickedFoodInput.protein.toDoubleOrNull() != null) &&
                (pickedFoodInput.fat.toDoubleOrNull() != null) &&
                (pickedFoodInput.carbs.toDoubleOrNull() != null)
}

data class FoodInput(
    val name: String = Constants.EMPTY_STRING,
    val protein: String = Constants.EMPTY_STRING,
    val fat: String = Constants.EMPTY_STRING,
    val carbs: String = Constants.EMPTY_STRING,
    val imageUri: String? = null
)