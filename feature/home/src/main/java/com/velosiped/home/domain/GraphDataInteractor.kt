package com.velosiped.home.domain

import com.velosiped.training.traininghistory.repository.TrainingHistory
import com.velosiped.training.traininghistory.repository.TrainingHistoryRepository
import com.velosiped.ui.model.graph.GraphData
import com.velosiped.ui.model.graph.GraphDataValue
import com.velosiped.utility.extensions.DOT
import com.velosiped.utility.extensions.EMPTY
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.ZERO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

class GraphDataInteractor @Inject constructor(
    private val trainingHistoryRepository: TrainingHistoryRepository
) {
    private val _graphData = MutableSharedFlow<GraphData>()
    val graphData = _graphData.asSharedFlow()

    private var graphUpdateJob: Job? = null

    suspend fun startCyclingData() {
        trainingHistoryRepository.getAllTrainingHistory().collect { trainingHistory ->
            graphUpdateJob?.cancel()

            val trainingDataSet = getGraphDataSet(trainingHistory).filter { it.value.size > Int.ONE }
            val exerciseList = trainingDataSet.keys.toList()
            val maxIndex = exerciseList.lastIndex

            var currentIndex = Int.ZERO

            while (true) {
                val currentExercise = exerciseList.getOrElse(currentIndex) { String.EMPTY }
                val currentGraphValues = trainingDataSet[currentExercise] ?: emptyList()

                _graphData.emit(
                    GraphData(
                        exercise = currentExercise,
                        values = currentGraphValues
                    )
                )

                delay(GRAPH_DATA_UPDATE_DELAY)

                if (currentIndex < maxIndex) currentIndex++
                else currentIndex = Int.ZERO
            }
        }
    }

    private fun getGraphDataSet(trainingHistory: List<TrainingHistory>) = trainingHistory
        .groupBy { it.exercise }
        .mapValues { (_, values) ->
            values.groupBy { Triple(it.day, it.month, it.year) }
                .map { (dateTriple, items) ->
                    val date = buildString {
                        append(dateTriple.first)
                        append(String.DOT)
                        append(dateTriple.second)
                        append(String.DOT)
                        append(dateTriple.third)
                    }
                    val volume = items.sumOf { it.reps * it.weight.toDouble() }
                    GraphDataValue(
                        date = date,
                        calculatedValue = volume
                    )
                }
        }

    companion object {
        const val GRAPH_DATA_UPDATE_DELAY = 5000L
    }

}