package com.velosiped.programmanager.presentation

import com.velosiped.programmanager.presentation.utils.ProgramItemState
import com.velosiped.training.exercise.repository.Exercise
import com.velosiped.training.program.repository.Program

data class ProgramEditUiState(
    val programList: List<Program> = emptyList(),
    val exerciseList: List<Exercise> = emptyList(),
    val programItemState: ProgramItemState = ProgramItemState()
)