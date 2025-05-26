package com.velosiped.notes.presentation.screens.statisticsScreen

import com.velosiped.notes.domain.GraphDataFormula

sealed interface StatisticsUiAction {
    data class SetFormula(val formula: GraphDataFormula): StatisticsUiAction
    data class SetExercise(val exercise: String): StatisticsUiAction
}