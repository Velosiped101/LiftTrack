package com.example.notes.presentation.screens.training.programExecScreen

import com.example.notes.data.local.program.Program
import com.example.notes.data.local.saveddata.program.ProgramProgress

data class ProgramExecUiState(
    val program: MutableList<Program> = mutableListOf(), // TODO: Change to immutable list
    val programProgress: MutableList<ProgramProgress> = mutableListOf(), // TODO: and you too
    val currentStep: Int = 0,
) {
    val currentProgramProgressStep: ProgramProgress
        get() = programProgress[currentStep]
    val currentProgramStep: Program
        get() = program[currentStep]
}