package com.velosiped.notes.presentation.screens.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.data.repository.diet.DietRepository
import com.velosiped.notes.data.repository.program.ProgramRepository
import com.velosiped.notes.data.repository.tempProgress.AppProtoDataStoreRepository
import com.velosiped.notes.data.worker.WorkScheduler
import com.velosiped.notes.domain.GetGraphDataUseCase
import com.velosiped.notes.domain.GraphDataFormula
import com.velosiped.notes.domain.ProgramData
import com.velosiped.notes.proto.AppPreferences
import com.velosiped.notes.utils.DayProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dietRepository: DietRepository,
    private val programRepository: ProgramRepository,
    private val getGraphDataUseCase: GetGraphDataUseCase,
    private val appProtoDataStoreRepository: AppProtoDataStoreRepository,
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
            combine(
                dietRepository.getMealHistory(),
                programRepository.getProgramForToday(),
                appProtoDataStoreRepository.appProtoStoreFlow,
                getGraphDataUseCase(GraphDataFormula.Volume)
            ) { mealHistory, program, dataStore, graphData ->
                val programExecLocked = dataStore.programExecLock.programExecutionLocked
                val programTempProgress = dataStore.protoProgramProgressItemsList
                val dayProgress = when {
                    programExecLocked -> DayProgress.TrainingFinished
                    !programExecLocked && program.isEmpty() -> DayProgress.Rest
                    else -> DayProgress.Training
                }
                val prefs = dataStore.appPreferences
                MainScreenData(
                    mealHistory = mealHistory,
                    dayProgress = dayProgress,
                    prefs = prefs,
                    graphData = graphData
                )
            }.distinctUntilChanged().collect { data ->
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
        val exercises = graphData.keys.toList()
        if (exercises.isEmpty()) return

        graphJob = viewModelScope.launch {
            val lastIndex = exercises.lastIndex
            var currentIndex = 0
            while (true) {
                val currentExercise = exercises[currentIndex]
                val currentValues = graphData[currentExercise]?.map { it.value ?: 0.0 } ?: emptyList()
                val dateTriples = graphData[currentExercise]?.map { it.date } ?: emptyList()
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

                _uiState.update {
                    it.copy(
                        currentExercise = currentExercise,
                        currentValues = currentValues,
                        dates = dates
                    )
                }
                delay(7000L)
                if (currentIndex < lastIndex) currentIndex++
                else currentIndex = 0
            }
        }
    }

    private fun checkForProgramUpdate() {
        viewModelScope.launch {
            val changesFound = combine(
                programRepository.getProgramForToday(),
                appProtoDataStoreRepository.appProtoStoreFlow
            ){ programList, dataStore ->
                val savedProgress = dataStore.protoProgramProgressItemsList
                if (savedProgress.isEmpty()) return@combine false
                val differentSize = (programList.size != savedProgress.size)
                if (differentSize) return@combine true
                val differentExercises = programList.zip(savedProgress).any { (program, savedProgram) ->
                    program.exercise != savedProgram.exercise
                }
                val differentRepsPlanned = programList.zip(savedProgress).any { (program, savedProgram) ->
                    program.reps != savedProgram.repsPlanned
                }
                Log.e("ITOGO", "$differentExercises, $differentRepsPlanned")
                differentExercises || differentRepsPlanned
            }.first()
            _changesFound.emit(changesFound)
        }
    }

    private fun resetProgramProgress() {
        viewModelScope.launch {
            appProtoDataStoreRepository.resetProgramProgress()
        }
    }

    data class MainScreenData(
        val mealHistory: List<MealHistory>,
        val dayProgress: DayProgress,
        val prefs: AppPreferences,
        val graphData: Map<String, List<ProgramData>>
    )
}