package com.velosiped.notes.presentation.screens.main

import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.utils.DayProgress
import com.velosiped.notes.utils.GraphData
import com.velosiped.notes.utils.cut

data class MainScreenUiState(
    val mealHistory: List<MealHistory> = listOf(),
    val dayProgress: DayProgress = DayProgress.Rest,
    val targetCalories: Int = 2500,
    val currentGraphData: GraphData = GraphData()
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