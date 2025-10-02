package com.velosiped.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.datastore.DataStoreRepository
import com.velosiped.diet.mealhistory.repository.MealHistoryRepository
import com.velosiped.home.domain.GraphDataInteractor
import com.velosiped.home.presentation.utils.TrainingState
import com.velosiped.training.program.repository.ProgramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mealHistoryRepository: MealHistoryRepository,
    private val programRepository: ProgramRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val graphDataInteractor: GraphDataInteractor
): ViewModel() {
    private val _uiState: MutableStateFlow<HomeScreenUiState> = MutableStateFlow(HomeScreenUiState())
        val uiState = _uiState.asStateFlow()

    private val _programChangesFound = MutableSharedFlow<Boolean>()
        val programChangesFound = _programChangesFound.asSharedFlow()

    private val _programResetDone = MutableSharedFlow<Unit>()
        val programResetDone = _programResetDone.asSharedFlow()

    init {
        observeTargetCalories()
        observeMealHistory()
        observeTrainingState()
        cycleGraphData()
    }

    fun checkForProgramUpdate() {
        viewModelScope.launch {
            val changesFound = combine(
                programRepository.getProgramForToday(),
                dataStoreRepository.appProtoStoreFlow
            ) { programForToday, dataStore ->
                val savedProgress = dataStore.protoProgramProgressItemsList
                if (savedProgress.isEmpty()) return@combine false

                val differentSize = (programForToday.size != savedProgress.size)
                if (differentSize) return@combine true

                val differentExercises =
                    programForToday.zip(savedProgress).any { (program, savedProgram) ->
                        program.exercise != savedProgram.exercise
                    }

                val differentRepsPlanned =
                    programForToday.zip(savedProgress).any { (program, savedProgram) ->
                        program.reps != savedProgram.repsPlanned
                    }

                differentExercises || differentRepsPlanned
            }.first()
            _programChangesFound.emit(changesFound)
        }
    }

    fun resetProgramProgress() {
        viewModelScope.launch {
            dataStoreRepository.resetProgramProgress()
            _programResetDone.emit(Unit)
        }
    }

    fun observeMealHistory() {
        viewModelScope.launch {
            mealHistoryRepository.getMealHistory().collect { mealHistory ->
                _uiState.update { it.copy(mealHistory = mealHistory) }
            }
        }
    }

    fun observeTrainingState() {
        viewModelScope.launch {
            combine(
                dataStoreRepository.appProtoStoreFlow,
                programRepository.getProgramForToday()
            ) { prefs, programForToday ->
                val programExecLocked = prefs.programExecLock.programExecutionLocked
                val trainingState = when {
                    programExecLocked -> TrainingState.DONE
                    !programExecLocked && programForToday.isNotEmpty() -> TrainingState.AWAITS
                    else -> TrainingState.REST
                }
                trainingState
            }.collect { trainingState ->
                _uiState.update { it.copy(trainingState = trainingState) }
            }
        }
    }

    fun observeTargetCalories() {
        viewModelScope.launch {
            dataStoreRepository.appProtoStoreFlow.collect { dataStore ->
                val targetCalories = dataStore.appPreferences.targetCalories
                _uiState.update { it.copy(targetCalories = targetCalories) }
            }
        }
    }

    fun cycleGraphData() {
        viewModelScope.launch {
            graphDataInteractor.startCyclingData()
        }
        viewModelScope.launch {
            graphDataInteractor.graphData.collect { data ->
                _uiState.update { it.copy(currentGraphData = data) }
            }
        }
    }
}