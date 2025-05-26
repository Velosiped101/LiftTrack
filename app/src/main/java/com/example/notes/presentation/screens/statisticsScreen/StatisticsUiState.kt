package com.example.notes.presentation.screens.statisticsScreen

import androidx.compose.ui.graphics.Color
import com.example.notes.domain.GraphDataFormula
import com.example.notes.domain.ProgramData

data class StatisticsUiState(
    val formula: GraphDataFormula = GraphDataFormula.Volume,
    val exercise: String? = null,
    val exercises: List<String> = listOf(),
    val values: List<ProgramData> = listOf(),
    val dates: List<String> = listOf()
)

data class LabeledGraphData(
    val values: List<Double>,
    val label: String,
    val color: Color
)