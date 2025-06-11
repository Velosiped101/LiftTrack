package com.velosiped.notes.domain.usecase.main

import com.velosiped.notes.domain.usecase.statistics.ProgramData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CycleGraphDataUseCase {
    operator fun invoke(
        graphData: Map<String, List<ProgramData>>,
        onUpdate: (exercise: String, values: List<Double>, dates: List<String>) -> Unit
    ): Job? {
        if (graphData.isEmpty()) return null

        val exercises = graphData.keys.toList()
        val lastIndex = exercises.lastIndex

        return CoroutineScope(Dispatchers.Default).launch {
            var currentIndex = 0
            while (isActive) {
                val currentExercise = exercises[currentIndex]
                val currentValues = graphData[currentExercise]?.map { it.value ?: 0.0 } ?: emptyList()
                val dateTriples = graphData[currentExercise]?.map { it.date } ?: emptyList()
                val dates = dateTriples.map {
                    val (day, month, year) = it
                    val d = day.toString().padStart(2, '0')
                    val m = month.toString().padStart(2, '0')
                    val y = year.toString().takeLast(2)
                    "$d.$m.$y"
                }
                onUpdate(currentExercise, currentValues, dates)
                delay(7000L)
                currentIndex = if (currentIndex < lastIndex) currentIndex + 1 else 0
            }
        }
    }
}
