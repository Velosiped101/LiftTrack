package com.example.notes.presentation.screens.training.programExecScreen

import com.example.notes.data.local.saveddata.program.ProgramProgress

sealed interface ProgramExecUiAction {
    data object GoToNextStep: ProgramExecUiAction
    data object GoToPreviousStep: ProgramExecUiAction
    data class ChangeRepsDone(val reps: Float): ProgramExecUiAction
    data object SaveProgress: ProgramExecUiAction
}