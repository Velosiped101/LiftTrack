package com.velosiped.statistic.presentation

import com.velosiped.statistic.presentation.model.StatsData
import com.velosiped.statistic.presentation.utils.GraphDataFormula

data class StatisticsUiState(
    val currentFormula: GraphDataFormula = GraphDataFormula.VOLUME,
    val currentExercise: String? = null,
    val exercises: List<String> = emptyList(),
    val data: StatsData = StatsData()
)