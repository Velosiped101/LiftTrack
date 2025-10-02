package com.velosiped.programmanager.presentation.utils

import com.velosiped.training.program.repository.Program

fun Program.toProgramItemState() = ProgramItemState(
    id = this.id,
    exercise = this.exercise,
    day = DayOfWeek.entries.find { it.toString() == this.day } ?: DayOfWeek.MONDAY,
    reps = this.reps
)

fun ProgramItemState.toProgram() = Program(
    id = this.id,
    exercise = this.exercise,
    day = this.day.toString(),
    reps = this.reps
)