package com.velosiped.notes.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.notes.data.worker.WorkScheduler
import com.velosiped.notes.domain.usecase.main.MainUseCase
import com.velosiped.notes.domain.usecase.statistics.ProgramData
import com.velosiped.notes.utils.GraphData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: MainUseCase,
    private val workScheduler: WorkScheduler
): ViewModel() {
    private val _uiState: MutableStateFlow<MainScreenUiState> = MutableStateFlow(MainScreenUiState())
        val uiState = _uiState.asStateFlow()

    private val _programChangesFound = MutableSharedFlow<Boolean>()
    val programChangesFound = _programChangesFound.asSharedFlow()

    private val _programResetDone = MutableSharedFlow<Unit>()
    val programResetDone = _programChangesFound.asSharedFlow()

    private var graphUpdateJob: Job? = null

    init {
        viewModelScope.launch {
            useCase.observeMainScreenInformationUseCase().collect { data ->
                _uiState.update {
                    it.copy(
                        mealHistory = data.mealHistory,
                        trainingState = data.trainingState,
                        targetCalories = data.prefs.targetCalories
                    )
                }
                workScheduler.initWorker(
                    resetTimeHour = data.prefs.resetTimeHour,
                    resetTimeMinute = data.prefs.resetTimeMinute
                )
                startUpdatingGraphData(data.graphData)
            }
        }
    }

    fun checkForProgramUpdate() {
        viewModelScope.launch {
            val changesFound = useCase.checkForProgramUpdateUseCase()
            _programChangesFound.emit(changesFound)
        }
    }

    fun resetProgramProgress() {
        viewModelScope.launch {
            useCase.resetProgramProgressUseCase()
            _programResetDone.emit(Unit)
        }
    }

    private fun startUpdatingGraphData(graphData: Map<String, List<ProgramData>>) {
        graphUpdateJob?.cancel()
        graphUpdateJob = useCase.cycleGraphDataUseCase(graphData) { exercise, values, dates ->
            val currentGraphData = GraphData(
                exercise = exercise,
                values = values,
                dates = dates
            )
            _uiState.update {
                it.copy(
                    currentGraphData = currentGraphData
                )
            }
        }
    }
}