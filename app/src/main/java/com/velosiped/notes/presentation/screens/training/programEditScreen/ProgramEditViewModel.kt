package com.velosiped.notes.presentation.screens.training.programEditScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.notes.data.database.exercise.Exercise
import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.data.repository.exercise.ExerciseRepository
import com.velosiped.notes.data.repository.program.ProgramRepository
import com.velosiped.notes.utils.DayOfWeek
import com.velosiped.notes.utils.EMPTY_STRING
import com.velosiped.notes.utils.ExerciseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgramEditViewModel @Inject constructor(
    private val programRepository: ProgramRepository,
    private val exerciseRepository: ExerciseRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<ProgramEditUiState> = MutableStateFlow(ProgramEditUiState())
        val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val exercises = exerciseRepository.getAll().first()
            programRepository.getAll().collect { programList ->
                _uiState.update {
                    it.copy(programList = programList, exerciseList = exercises)
                }
            }
        }
    }

    fun uiAction(action: ProgramEditUiAction) {
        when (action) {
            is ProgramEditUiAction.SelectExerciseType -> setExerciseType(action.exerciseType)
            is ProgramEditUiAction.SelectNewExercise -> selectNewExercise(action.newExercise)
            is ProgramEditUiAction.SelectProgramDay -> setProgramDay(action.dayOfWeek)
            is ProgramEditUiAction.SelectProgramExercise -> selectProgramExercise(action.programExercise)
            is ProgramEditUiAction.ChangeReps -> changeReps(action.reps)
            is ProgramEditUiAction.ChangeSets -> changeSets(action.sets)
            is ProgramEditUiAction.DeleteFromProgram -> deleteFromProgram()
            ProgramEditUiAction.InsertToProgram -> insertToProgram()
            ProgramEditUiAction.DropProgram -> dropProgram()
            ProgramEditUiAction.DropProgramForDay -> dropProgramForDay()
        }
    }

    private fun setExerciseType(exerciseType: ExerciseType) {
        _uiState.update {
            it.copy(exerciseType = exerciseType)
        }
    }

    private fun setProgramDay(day: DayOfWeek) {
        _uiState.update {
            it.copy(day = day)
        }
    }

    private fun selectNewExercise(exercise: Exercise) {
        _uiState.update {
            val newProgramExercise = _uiState.value.selectedProgramItem?.copy(exercise = exercise.name, reps = 1)
            it.copy(
                selectedProgramItem = newProgramExercise,
                sets = 1
            )
        }
    }

    private fun selectProgramExercise(programExercise: Program?) {
        val programItem = programExercise ?: Program(
            exercise = EMPTY_STRING,
            dayOfWeek = _uiState.value.day.name,
            reps = 1
        )
        _uiState.update {
            it.copy(
                selectedProgramItem = programItem,
                sets = 1,
            )
        }
    }

    private fun changeSets(sets: Float) {
        _uiState.update {
            it.copy(sets = sets.toInt())
        }
    }

    private fun changeReps(reps: Float) {
        val pickedProgramExercise = _uiState.value.selectedProgramItem
        val updatedProgramExercise = pickedProgramExercise?.copy(reps = reps.toInt())
        _uiState.update {
            it.copy(selectedProgramItem = updatedProgramExercise)
        }
    }

    private fun insertToProgram() {
        val item = _uiState.value.selectedProgramItem ?: return
        viewModelScope.launch {
            repeat(_uiState.value.sets) {
                programRepository.insertToProgram(item)
            }
        }
    }

    private fun deleteFromProgram() {
        val item = _uiState.value.selectedProgramItem ?: return
        viewModelScope.launch {
            programRepository.deleteFromProgram(item)
        }
    }

    private fun dropProgramForDay() {
        viewModelScope.launch {
            programRepository.dropProgramForDay(_uiState.value.day.name)
        }
    }

    private fun dropProgram() {
        viewModelScope.launch {
            programRepository.dropProgram()
        }
    }
}