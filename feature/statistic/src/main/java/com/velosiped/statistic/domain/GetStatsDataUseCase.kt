package com.velosiped.statistic.domain

import com.velosiped.statistic.presentation.model.StatsData
import com.velosiped.statistic.presentation.model.TableData
import com.velosiped.statistic.presentation.model.TableDataValue
import com.velosiped.statistic.presentation.utils.GraphDataFormula
import com.velosiped.training.traininghistory.repository.TrainingHistoryRepository
import com.velosiped.ui.model.graph.GraphData
import com.velosiped.ui.model.graph.GraphDataValue
import com.velosiped.utility.extensions.DOT
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

class GetStatsDataUseCase @Inject constructor(
    private val trainingHistoryRepository: TrainingHistoryRepository
) {
    suspend operator fun invoke(formula: GraphDataFormula, exercise: String): StatsData {
        val trainingHistory = trainingHistoryRepository.getAllTrainingHistory().first()
        if (trainingHistory.isEmpty()) return StatsData(null, null)
        val exerciseData = trainingHistory.filter { it.exercise == exercise }
        val groupedData = exerciseData.groupBy { Triple(it.day, it.month, it.year) }

        return when (formula) {
            GraphDataFormula.VOLUME -> {
                val graphValues = groupedData.map { (date, values) ->
                        GraphDataValue(
                            date = formatDate(date),
                            calculatedValue = values.sumOf { it.reps * it.weight.toDouble() }
                        )
                    }
                StatsData(
                    graphData = GraphData(
                        exercise = exercise,
                        values = graphValues
                    ),
                    tableData = null
                )
            }
            GraphDataFormula.ONE_REP_MAX -> {
                val graphValues = groupedData.map { (date, values) ->
                        GraphDataValue(
                            date = formatDate(date),
                            calculatedValue = values.maxOf { it.weight * (1 + it.reps / 30.0) }
                        )
                    }
                StatsData(
                    graphData = GraphData(
                        exercise = exercise,
                        values = graphValues
                    ),
                    tableData = null
                )
            }
            GraphDataFormula.RAW -> {
                val tableValues = exerciseData.map {
                    val date = formatDate(Triple(it.day, it.month, it.year))
                        TableDataValue(
                            date = date,
                            repsPlanned = it.repsPlanned,
                            repsDone = it.reps,
                            weight = it.weight
                        )
                    }
                StatsData(
                    graphData = null,
                    tableData = TableData(
                        exercise = exercise,
                        values = tableValues
                    )
                )
            }
        }
    }

    private fun formatDate(date: Triple<Int, Int, Int>): String = buildString {
        append(date.first)
        append(String.DOT)
        append(date.second)
        append(String.DOT)
        append(date.third)
    }
}