package com.velosiped.notes.presentation.screens.training.programExecScreen

sealed interface ProgramExecUiAction {
    data class ChangeRepsDone(val index: Int, val reps: Float): ProgramExecUiAction
    data class ChangeWeight(val index: Int, val weight: Float): ProgramExecUiAction
    data object SaveProgress: ProgramExecUiAction
    data object UpdateStoredProgress: ProgramExecUiAction
}