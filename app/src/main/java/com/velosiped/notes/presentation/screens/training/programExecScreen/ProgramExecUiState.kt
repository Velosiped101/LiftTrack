package com.velosiped.notes.presentation.screens.training.programExecScreen

import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress

data class ProgramExecUiState(
    val programProgress: List<ProgramProgress>? = null,
    val showHintList: List<Boolean>? = null,
    val initialWeight: List<Float>? = null
)