package com.example.notes.presentation.screens.training.programEditScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.Service
import com.example.notes.data.ExerciseRepository
import com.example.notes.data.ProgramRepository
import com.example.notes.data.local.program.Exercise
import com.example.notes.data.local.program.Program
import com.example.notes.utils.DayOfWeek
import com.example.notes.utils.ExerciseType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgramEditViewModel(
    private val programRepository: ProgramRepository = Service.programRepository,
    private val exerciseRepository: ExerciseRepository = Service.exerciseRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<ProgramEditUiState> = MutableStateFlow(ProgramEditUiState())
        val uiState = _uiState.asStateFlow()

    init {
        val program = programRepository.getAll()
        val exercises = exerciseRepository.getAll()
        viewModelScope.launch {
            program.collect { programList ->
                exercises.collect { exerciseList ->
                    _uiState.update {
                        it.copy(programList = programList, exerciseList = exerciseList)
                    }
                }
            }
        }
    }

    fun uiAction(action: ProgramEditUiAction) {
        when (action) {
            is ProgramEditUiAction.SelectExerciseType -> selectExerciseType(action.exerciseType)
            is ProgramEditUiAction.SelectNewExercise -> selectNewExercise(action.newExercise)
            is ProgramEditUiAction.SelectProgramDay -> selectProgramDay(action.dayOfWeek)
            is ProgramEditUiAction.SelectProgramExercise -> selectProgramExercise(action.programExercise)
            is ProgramEditUiAction.ChangeReps -> changeReps(action.reps)
            is ProgramEditUiAction.ChangeSets -> changeSets(action.sets)
            is ProgramEditUiAction.DeleteFromProgram -> deleteFromProgram()
            ProgramEditUiAction.InsertToProgram -> insertToProgram()
            ProgramEditUiAction.NavigateBackFromSetter -> navigateBackFromSetter()
            ProgramEditUiAction.DismissDialog -> dismissDialog()
        }
    }

    private fun selectExerciseType(exerciseType: ExerciseType) {
        _uiState.update {
            it.copy(selectedExerciseType = exerciseType)
        }
    }

    private fun selectProgramDay(day: DayOfWeek) {
        _uiState.update {
            it.copy(selectedProgramDay = day)
        }
    }

    private fun selectNewExercise(newExercise: Exercise) {
        _uiState.update {
            it.copy(
                selectedNewExercise = newExercise,
                isInSetter = true,
                sets = 0f,
                reps = 0f
            )
        }
    }

    private fun selectProgramExercise(programExercise: Program?) {
        _uiState.update {
            val isInSetter = programExercise != null
            it.copy(
                selectedProgramExercise = programExercise,
                isDialogActive = true,
                isInSetter = isInSetter
            )
        }
    }

    private fun changeSets(sets: Float) {
        _uiState.update {
            it.copy(sets = sets)
        }
    }

    private fun changeReps(reps: Float) {
        _uiState.update {
            it.copy(reps = reps)
        }
    }

    private fun insertToProgram() {
        val item = Program(
            dayOfWeek = _uiState.value.selectedProgramDay.name,
            reps = _uiState.value.reps.toInt(),
            exercise = _uiState.value.selectedNewExercise?.name ?: return
        )
        viewModelScope.launch {
            repeat(_uiState.value.sets.toInt()) {
                programRepository.insertToProgram(item)
            }
        }
        _uiState.update {
            it.copy(isDialogActive = false, isInSetter = false)
        }
    }

    private fun deleteFromProgram() {
        val item = _uiState.value.selectedProgramExercise ?: return
        viewModelScope.launch {
            programRepository.deleteFromProgram(item)
        }
        _uiState.update {
            it.copy(isDialogActive = false, isInSetter = false)
        }
    }

    private fun navigateBackFromSetter() {
        _uiState.update {
            if (_uiState.value.selectedProgramExercise == null) it.copy(isInSetter = false)
            else it.copy(isDialogActive = false, isInSetter = false)
        }
    }

    private fun dismissDialog() {
        _uiState.update {
            it.copy(isDialogActive = false, isInSetter = false)
        }
    }
}