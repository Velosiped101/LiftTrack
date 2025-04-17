package com.example.notes.presentation.screens.mainScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.utils.Date
import com.example.notes.utils.Nutrient
import java.math.BigDecimal
import java.math.RoundingMode

data class MainScreenUiState(
    val mealHistory: SnapshotStateList<MealHistory> = mutableStateListOf(),
    val isRestDay: Boolean = false,
) {
    val totalProtein: Double
        get() = getTotalNutrient(mealHistory, Nutrient.Protein)
    val totalFat: Double
        get() = getTotalNutrient(mealHistory, Nutrient.Fat)
    val totalCarbs: Double
        get() = getTotalNutrient(mealHistory, Nutrient.Carbs)
    val totalCals: Int
        get() = calculateTotalCals(totalProtein, totalFat, totalCarbs)

    private fun getTotalNutrient(mealHistory: SnapshotStateList<MealHistory>, nutrient: Nutrient): Double {
        val totalNutrient = when (nutrient) {
            Nutrient.Protein -> mealHistory.sumOf { it.protein * it.mass/100 }
            Nutrient.Fat -> mealHistory.sumOf { it.fat * it.mass/100 }
            Nutrient.Carbs -> mealHistory.sumOf { it.carbs * it.mass/100 }
        }
        return BigDecimal(totalNutrient).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    private fun calculateTotalCals(protein: Double, fat: Double, carbs: Double): Int {
        val cals = (protein + carbs) * 4 + fat * 9
        return cals.toInt()
    }
}