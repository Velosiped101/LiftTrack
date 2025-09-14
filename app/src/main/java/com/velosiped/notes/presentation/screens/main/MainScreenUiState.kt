package com.velosiped.notes.presentation.screens.main

import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.utils.TrainingState
import com.velosiped.notes.utils.GraphData
import com.velosiped.notes.utils.NutrientsIntake
import com.velosiped.notes.utils.ZERO
import com.velosiped.notes.utils.getNutrientsIntake

data class MainScreenUiState(
    val mealHistory: List<MealHistory> = emptyList(),
    val trainingState: TrainingState = TrainingState.REST,
    val targetCalories: Int = Int.ZERO,
    val currentGraphData: GraphData = GraphData()
) {
    val currentIntake: NutrientsIntake
        get() = mealHistory.getNutrientsIntake()
}