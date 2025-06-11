package com.velosiped.notes.presentation.screens.mainScreen

import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.utils.DayProgress
import com.velosiped.notes.utils.cut

data class MainScreenUiState(
    val mealHistory: List<MealHistory> = listOf(),
    val dayProgress: DayProgress = DayProgress.Rest,
    val targetCalories: Int = 2500,
    val currentExercise: String? = null,
    val currentValues: List<Double> = listOf(),
    val dates: List<String> = listOf()
) {
    val totalProtein: Double
        get() = mealHistory.sumOf { it.protein * it.mass/100 }.cut()
    val totalFat: Double
        get() = mealHistory.sumOf { it.fat * it.mass/100 }.cut()
    val totalCarbs: Double
        get() = mealHistory.sumOf { it.carbs * it.mass/100 }.cut()
    val totalCals: Int
        get() = ((totalProtein + totalCarbs) * 4 + totalFat * 9).toInt()
}