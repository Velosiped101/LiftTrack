package com.velosiped.notes.presentation.screens.training.programEditScreen

import com.velosiped.notes.data.database.exercise.Exercise
import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.utils.DayOfWeek
import com.velosiped.notes.utils.ExerciseType

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