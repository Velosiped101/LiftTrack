package com.example.notes.presentation.screens.statisticsScreen

import com.example.notes.domain.GraphDataFormula

sealed interface StatisticsUiAction {
    data class SetFormula(val formula: GraphDataFormula): StatisticsUiAction
    data class SetExercise(val exercise: String): StatisticsUiAction
}