package com.velosiped.home.presentation

import com.velosiped.diet.mealhistory.repository.MealHistory
import com.velosiped.home.presentation.utils.TrainingState
import com.velosiped.ui.model.graph.GraphData
import com.velosiped.utility.data.NutrientsIntake
import com.velosiped.utility.extensions.EMPTY
import com.velosiped.utility.extensions.HUNDRED
import com.velosiped.utility.extensions.ZERO
import kotlin.collections.sumOf

data class HomeScreenUiState(
    val mealHistory: List<MealHistory> = emptyList(),
    val trainingState: TrainingState = TrainingState.REST,
    val targetCalories: Int = Int.ZERO,
    val currentGraphData: GraphData = GraphData(exercise = String.EMPTY, emptyList())
) {
    val currentIntake: NutrientsIntake
        get() = mealHistory.let {
            NutrientsIntake(
                protein = it.sumOf { it.protein.toDouble() * it.mass / Int.HUNDRED }.toFloat(),
                fat = it.sumOf { it.fat.toDouble() * it.mass / Int.HUNDRED }.toFloat(),
                carbs = it.sumOf { it.carbs.toDouble() * it.mass / Int.HUNDRED }.toFloat()
            )
        }
}