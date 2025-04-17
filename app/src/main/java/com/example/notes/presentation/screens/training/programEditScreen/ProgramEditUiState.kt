package com.example.notes.presentation.screens.training.programEditScreen

import com.example.notes.data.local.program.Exercise
import com.example.notes.data.local.program.Program
import com.example.notes.utils.DayOfWeek
import com.example.notes.utils.ExerciseType

data class ProgramEditUiState(
    val programList: List<Program> = listOf(),
    val selectedProgramDay: DayOfWeek = DayOfWeek.Monday,
    val exerciseList: List<Exercise> = listOf(),
    val selectedExerciseType: ExerciseType = ExerciseType.All,
    val selectedProgramExercise: Program? = null,
    val selectedNewExercise: Exercise? = null,
    val isDialogActive: Boolean = false,
    val isInSetter: Boolean = false,
    val sets: Float = 0f,
    val reps: Float = 0f
    ) {
    val programForSelectedDay: List<Program>
        get() = programList.filter { it.dayOfWeek == selectedProgramDay.name }
    val exercisesForSelectedType: List<Exercise>
        get() = if (selectedExerciseType != ExerciseType.All)
            exerciseList.filter { it.type == selectedExerciseType.name } else exerciseList
}