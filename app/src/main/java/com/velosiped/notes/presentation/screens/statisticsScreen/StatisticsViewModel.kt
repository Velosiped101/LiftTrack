package com.velosiped.notes.presentation.screens.statisticsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.notes.domain.GetGraphDataUseCase
import com.velosiped.notes.domain.GraphDataFormula
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getGraphData: GetGraphDataUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<StatisticsUiState> = MutableStateFlow(StatisticsUiState())
        val uiState = _uiState.asStateFlow()
    fun uiAction(action: StatisticsUiAction) {
        when (action) {
            is StatisticsUiAction.SetFormula -> setFormula(action.formula)
            is StatisticsUiAction.SetExercise -> setExercise(action.exercise)
        }
    }

    init {
        updateState(formula = _uiState.value.formula, exercise = null)
    }


    private fun setFormula(formula: GraphDataFormula) {
        _uiState.update {
            it.copy(formula = formula)
        }
        updateState(formula = formula, exercise = _uiState.value.exercise)
    }

    private fun setExercise(exercise: String) {
        _uiState.update {
            it.copy(exercise = exercise)
        }
        updateState(formula = _uiState.value.formula, exercise = exercise)
    }

    private fun updateState(formula: GraphDataFormula, exercise: String?) {
        viewModelScope.launch {
            getGraphData(formula)
                .collectLatest { graphMap ->
                    val currentExercise = exercise ?: if (graphMap.isEmpty()) return@collectLatest else graphMap.keys.first().ifEmpty { return@collectLatest }
                    val dateTriples = graphMap[currentExercise]?.map { it.date } ?: emptyList()
                    val dates = dateTriples.map {
                        val dateDay = it.first.let { dayNumber ->
                            if (dayNumber < 10) "0$dayNumber" else dayNumber.toString()
                        }
                        val dateMonth = it.second.let { monthNumber ->
                            if (monthNumber < 10) "0$monthNumber" else monthNumber.toString()
                        }
                        val dateYear = it.third.toString().slice(2..3)
                        "$dateDay.$dateMonth.$dateYear"
                    }

                    if (exercise == null) {
                        val exercises = graphMap.keys.toList()
                        _uiState.update {
                            it.copy(
                                exercises = exercises,
                                dates = dates
                            )
                        }
                    }

                    val values = graphMap.get(currentExercise) ?: emptyList()
                    _uiState.update {
                        it.copy(
                            dates = dates,
                            exercise = currentExercise,
                            values = values
                        )
                    }
                }
        }
    }
}