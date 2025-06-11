package com.velosiped.notes.domain.usecase.statistics

import com.velosiped.notes.R
import com.velosiped.notes.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetGraphDataUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {
    operator fun invoke(formula: GraphDataFormula): Flow<Map<String, List<ProgramData>>> {
        return programRepository.getProgramProgress().map { programProgressList ->
            programProgressList
                .groupBy { it.exercise }
                .mapValues { (_, list) ->
                    list.groupBy { Triple(it.day, it.month, it.year) }
                        .map { (dateTriple, items) ->
                            when (formula) {

                                GraphDataFormula.Volume -> {
                                    val volume = items.sumOf { it.reps * it.weight.toDouble() }
                                    ProgramData(
                                        date = dateTriple,
                                        value = volume,
                                        rawData = null
                                    )
                                }

                                GraphDataFormula.OneRepMax -> {
                                    val oneRepMax = items.maxOf { it.weight * (1 + it.reps / 30.0) }
                                    ProgramData(
                                        date = dateTriple,
                                        value = oneRepMax,
                                        rawData = null
                                    )
                                }

                                GraphDataFormula.Raw -> {
                                    val reps = items.map { it.reps.toDouble() }
                                    val repsPlanned = items.map { it.repsPlanned.toDouble() }
                                    val weight = items.map { it.weight.toDouble() }
                                    ProgramData(
                                        date = dateTriple,
                                        value = null,
                                        rawData = RawGraphData(
                                            reps = reps,
                                            repsPlanned = repsPlanned,
                                            weight = weight
                                        )
                                    )
                                }

                            }
                        }
                }
        }
    }
}

data class ProgramData(
    val date: Triple<Int, Int, Int>,
    val value: Double?,
    val rawData: RawGraphData?
)

enum class GraphDataFormula(val textId: Int) {
    Volume(R.string.formula_volume),
    OneRepMax(R.string.formula_one_rep_max),
    Raw(R.string.formula_raw)
}

data class RawGraphData(
    val reps: List<Double>,
    val repsPlanned: List<Double>,
    val weight: List<Double>
)