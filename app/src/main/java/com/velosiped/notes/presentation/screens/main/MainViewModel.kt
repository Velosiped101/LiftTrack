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
    fun uiAction(action: MainScreenUiAction) {
        when (action) {
            MainScreenUiAction.CheckForProgramUpdate -> checkForProgramUpdate()
            MainScreenUiAction.ResetProgramProgress -> resetProgramProgress()
        }
    }

    private val _changesFound = MutableSharedFlow<Boolean>()
    val changesFound = _changesFound.asSharedFlow()

    private var graphJob: Job? = null

    init {
        viewModelScope.launch {
            useCase.observeMainScreenInformationUseCase().collect { data ->
                _uiState.update {
                    it.copy(
                        mealHistory = data.mealHistory,
                        dayProgress = data.dayProgress,
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

    private fun startUpdatingGraphData(graphData: Map<String, List<ProgramData>>) {
        graphJob?.cancel()
        graphJob = useCase.cycleGraphDataUseCase(graphData) { exercise, values, dates ->
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


    private fun checkForProgramUpdate() {
        viewModelScope.launch {
            val changesFound = useCase.checkForProgramUpdateUseCase()
            _changesFound.emit(changesFound)
        }
    }

    private fun resetProgramProgress() {
        viewModelScope.launch {
            useCase.resetProgramProgressUseCase()
        }
    }
}