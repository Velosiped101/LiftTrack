package com.velosiped.statistic.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.statistic.domain.GetExercisesFromHistoryUseCase
import com.velosiped.statistic.domain.GetStatsDataUseCase
import com.velosiped.statistic.presentation.utils.GraphDataFormula
import com.velosiped.utility.extensions.EMPTY
import com.velosiped.utility.extensions.ZERO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getStatsDataUseCase: GetStatsDataUseCase,
    private val getExercisesFromHistoryUseCase: GetExercisesFromHistoryUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<StatisticsUiState> = MutableStateFlow(StatisticsUiState())
        val uiState = _uiState.asStateFlow()

    init {
        setInitialState()
    }

    fun setFormula(formula: GraphDataFormula) {
        _uiState.update { it.copy(currentFormula = formula) }
        val exercise = uiState.value.currentExercise ?: uiState.value.exercises.let {
            if (it.isEmpty()) String.EMPTY else it.first()
        }
        updateState(
            formula = formula,
            exercise = exercise
        )
    }

    fun setExercise(exercise: String) {
        updateState(formula = uiState.value.currentFormula, exercise = exercise)
    }

    fun setInitialState() {
        viewModelScope.launch {
            val exercises = getExercisesFromHistoryUseCase()
            val statsData = getStatsDataUseCase(
                formula = GraphDataFormula.VOLUME,
                exercise = exercises.getOrNull(Int.ZERO) ?: String.EMPTY
            )
            _uiState.update {
                it.copy(
                    exercises = exercises,
                    data = statsData
                )
            }
        }
    }

    fun updateState(formula: GraphDataFormula, exercise: String) {
        viewModelScope.launch {
            val statsData = getStatsDataUseCase(
                formula = formula,
                exercise = exercise
            )
            _uiState.update { it.copy(data = statsData) }
        }
    }
}