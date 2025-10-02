package com.velosiped.programmanager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.programmanager.presentation.utils.DayOfWeek
import com.velosiped.programmanager.presentation.utils.ProgramItemState
import com.velosiped.programmanager.presentation.utils.toProgram
import com.velosiped.programmanager.presentation.utils.toProgramItemState
import com.velosiped.training.exercise.repository.Exercise
import com.velosiped.training.exercise.repository.ExerciseRepository
import com.velosiped.training.program.repository.Program
import com.velosiped.training.program.repository.ProgramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgramEditViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val programRepository: ProgramRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<ProgramEditUiState> = MutableStateFlow(ProgramEditUiState())
        val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val exercises = exerciseRepository.getAllExercises().first()
            programRepository.getAllProgramItems().collect { programList ->
                _uiState.update {
                    it.copy(programList = programList, exerciseList = exercises)
                }
            }
        }
    }

    fun onDayChange(day: DayOfWeek) {
        val updatedProgramItemState = uiState.value.programItemState.copy(day = day)
        _uiState.update { it.copy(programItemState = updatedProgramItemState) }
    }

    fun onProgramItemClick(item: Program) {
        val programItemState = item.toProgramItemState()
        _uiState.update {
            it.copy(
                programItemState = programItemState,
            )
        }
    }

    fun onAddNewClick() {
        val currentProgramItemState = uiState.value.programItemState
        val currentDay = currentProgramItemState.day
        val programItemState = ProgramItemState().copy(day = currentDay)
        _uiState.update { it.copy(programItemState = programItemState) }
    }

    fun onExerciseClick(exercise: Exercise) {
        val currentProgramItemState = uiState.value.programItemState
        val currentDay = currentProgramItemState.day
        val programItemState = ProgramItemState().copy(
            day = currentDay,
            exercise = exercise.name
        )
        _uiState.update {
            it.copy(programItemState = programItemState)
        }
    }

    fun onDeleteFromProgram() {
        viewModelScope.launch {
            programRepository.deleteFromProgram(uiState.value.programItemState.toProgram())
        }
    }

    fun onAddToProgram() {
        viewModelScope.launch {
            val programItem = uiState.value.programItemState.toProgram()
            val sets = uiState.value.programItemState.sets
            repeat(sets) {
                programRepository.insertToProgram(programItem)
            }
        }
    }

    fun onSetsChange(sets: Float) {
        val updatedProgramItemState = uiState.value.programItemState.copy(sets = sets.toInt())
        _uiState.update {
            it.copy(programItemState = updatedProgramItemState)
        }
    }

    fun onRepsChange(reps: Float) {
        val updatedProgramItemState = uiState.value.programItemState.copy(reps = reps.toInt())
        _uiState.update {
            it.copy(programItemState = updatedProgramItemState)
        }
    }

    fun onDropProgramForTheDay() {
        val day = uiState.value.programItemState.day.name
        viewModelScope.launch {
            programRepository.dropProgramForDay(day)
        }
    }

    fun onDropProgram() {
        viewModelScope.launch {
            programRepository.dropProgram()
        }
    }
}