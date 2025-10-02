package com.velosiped.training.program.databasemodel

import com.velosiped.training.program.repository.Program

internal fun ProgramEntity.toProgram() = Program(
    id = this.id,
    exercise = this.exercise,
    day = this.day,
    reps = this.reps
)

internal fun Program.toProgramEntity() = ProgramEntity(
    id = this.id,
    day = this.day,
    exercise = this.exercise,
    reps = this.reps
)