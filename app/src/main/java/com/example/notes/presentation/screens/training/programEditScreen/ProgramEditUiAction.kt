package com.example.notes.presentation.screens.training.programEditScreen

import com.example.notes.data.local.program.Exercise
import com.example.notes.data.local.program.Program
import com.example.notes.utils.DayOfWeek
import com.example.notes.utils.ExerciseType

sealed interface ProgramEditUiAction {
    data class SelectProgramDay(val dayOfWeek: DayOfWeek): ProgramEditUiAction
    data class SelectExerciseType(val exerciseType: ExerciseType): ProgramEditUiAction
    data class SelectProgramExercise(val programExercise: Program?): ProgramEditUiAction
    data class SelectNewExercise(val newExercise: Exercise): ProgramEditUiAction
    data class ChangeSets(val sets: Float): ProgramEditUiAction
    data class ChangeReps(val reps: Float): ProgramEditUiAction
    data object InsertToProgram: ProgramEditUiAction
    data object DeleteFromProgram: ProgramEditUiAction
    data object NavigateBackFromSetter: ProgramEditUiAction
    data object DismissDialog: ProgramEditUiAction
}