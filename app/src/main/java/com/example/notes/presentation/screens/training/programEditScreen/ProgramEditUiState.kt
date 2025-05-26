package com.example.notes.presentation.screens.training.programEditScreen

import com.example.notes.data.database.exercise.Exercise
import com.example.notes.data.database.program.Program
import com.example.notes.utils.DayOfWeek
import com.example.notes.utils.ExerciseType

data class ProgramEditUiState(
    val programList: List<Program>? = null,
    val selectedProgramItem: Program? = null,
    val exerciseList: List<Exercise> = listOf(),
    val day: DayOfWeek = DayOfWeek.Monday,
    val exerciseType: ExerciseType = ExerciseType.Back,
    val sets: Int = 1
    ) {
    val exercisesForSelectedType: List<Exercise>
        get() = exerciseList.filter { it.type == exerciseType.name }
}