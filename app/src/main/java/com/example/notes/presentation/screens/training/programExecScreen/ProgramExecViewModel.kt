package com.example.notes.presentation.screens.training.programExecScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.Service
import com.example.notes.data.ProgramRepository
import com.example.notes.data.local.program.Program
import com.example.notes.data.local.saveddata.program.ProgramProgress
import com.example.notes.utils.Date
import com.example.notes.utils.DayOfWeek
import com.example.notes.utils.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class ProgramExecViewModel(
    private val repository: ProgramRepository = Service.programRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<ProgramExecUiState> = MutableStateFlow(ProgramExecUiState())
        val uiState = _uiState.asStateFlow()
    fun uiAction(action: ProgramExecUiAction) {
        when (action) {
            ProgramExecUiAction.GoToNextStep -> goToNextStep()
            ProgramExecUiAction.GoToPreviousStep -> goToPreviousStep()
            is ProgramExecUiAction.ChangeRepsDone -> changeRepsDone(action.reps)
            ProgramExecUiAction.SaveProgress -> saveProgress()
        }
    }

    init {
        getEmptyProgramProgress()
    }

    private fun getEmptyProgramProgress() {
        viewModelScope.launch {
            val program = repository.getProgramForToday()
                .toMutableList().apply {
                    add(Program(dayOfWeek = "dada", exercise = "dumb ass", reps = 1488)) // TODO: Удалить
                    add(Program(dayOfWeek = "dada", exercise = "really", reps = 1337)) // TODO: Удалить
                }
            val programProgressEmptyItem = ProgramProgress(
                day = Date.day, month = Date.month, year = Date.year, reps = 0, exercise = EMPTY_STRING
            )
            _uiState.update { state ->
                val list = mutableListOf<ProgramProgress>()
                program.forEach {
                    list.add(programProgressEmptyItem.copy(exercise = it.exercise))
                }
                state.copy(
                    program = program,
                    programProgress = list
                )
            }
        }
    }

    private fun changeRepsDone(reps: Float) {
        _uiState.update {
            val currentStep = _uiState.value.currentStep
            val currentExerciseProgress = it.programProgress[currentStep].copy(reps = reps.toInt())
            val updatedProgramProgress = it.programProgress.apply {
                it.programProgress[currentStep] = currentExerciseProgress
            }
            it.copy(programProgress = updatedProgramProgress)
        }
    }

    private fun goToNextStep() {
        if (_uiState.value.currentStep == _uiState.value.programProgress.lastIndex) return
        _uiState.update {
            it.copy(currentStep = it.currentStep + 1)
        }
    }

    private fun goToPreviousStep() {
        if (_uiState.value.currentStep == 0) return
        _uiState.update {
            it.copy(currentStep = it.currentStep - 1)
        }
    }

    private fun saveProgress() {
        viewModelScope.launch {
            _uiState.value.programProgress.forEach {
                repository.insertToProgramProgress(it)
            }
        }
    }
}