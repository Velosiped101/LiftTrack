package com.velosiped.notes.presentation.screens.main

sealed interface MainScreenUiAction {
    data object CheckForProgramUpdate: MainScreenUiAction
    data object ResetProgramProgress: MainScreenUiAction
}