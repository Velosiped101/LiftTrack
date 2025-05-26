package com.velosiped.notes.presentation.screens.training.programEditScreen

import com.velosiped.notes.data.database.exercise.Exercise
import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.utils.DayOfWeek
import com.velosiped.notes.utils.ExerciseType

sealed interface ProgramEditUiAction {
    data class SelectProgramDay(val dayOfWeek: DayOfWeek): ProgramEditUiAction
    data class SelectExerciseType(val exerciseType: ExerciseType): ProgramEditUiAction
    data class SelectProgramExercise(val programExercise: Program?): ProgramEditUiAction
    data class SelectNewExercise(val newExercise: Exercise): ProgramEditUiAction
    data class ChangeSets(val sets: Float): ProgramEditUiAction
    data class ChangeReps(val reps: Float): ProgramEditUiAction
    data object InsertToProgram: ProgramEditUiAction
    data object DeleteFromProgram: ProgramEditUiAction
    data object DropProgramForDay: ProgramEditUiAction
    data object DropProgram: ProgramEditUiAction
}