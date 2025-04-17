package com.example.notes.presentation.screens.diet.foodManagerScreen

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.notes.data.local.food.Food

data class FoodManagerUiState(
    val foodList: SnapshotStateList<Food> = mutableStateListOf(),
    val isInDeleteMode: Boolean = false,
    val pickedFood: FoodInput = FoodInput(),
    val selectedForDeleteList: MutableList<Food> = mutableListOf(),
    val generatedUri: Uri? = null
) {
    val isFoodInputValid: Boolean
        get() = (pickedFood.name.isNotBlank()) &&
                (pickedFood.protein.toDoubleOrNull() != null) &&
                (pickedFood.fat.toDoubleOrNull() != null) &&
                (pickedFood.carbs.toDoubleOrNull() != null)
}