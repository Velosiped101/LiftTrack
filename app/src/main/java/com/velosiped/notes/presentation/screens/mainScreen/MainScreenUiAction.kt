package com.velosiped.notes.presentation.screens.mainScreen

sealed interface MainScreenUiAction {
    data object CheckForProgramUpdate: MainScreenUiAction
    data object ResetProgramProgress: MainScreenUiAction
}