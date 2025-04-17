package com.example.notes.presentation.screens.training.programExecScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.notes.data.local.program.Program
import com.example.notes.data.local.saveddata.program.ProgramProgress

@Composable
fun ProgramExecScreen(
    uiState: ProgramExecUiState,
    uiAction: (ProgramExecUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ProgramFragment(
            programItem = uiState.currentProgramStep,
            programProgressItem = uiState.currentProgramProgressStep,
            onValueChange = { uiAction(ProgramExecUiAction.ChangeRepsDone(it)) }
        )
        Text(
            text = "next",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { uiAction(ProgramExecUiAction.GoToNextStep) }
        )
        Text(
            text = "previous",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { uiAction(ProgramExecUiAction.GoToPreviousStep) }
        )
    }
}

@Composable
fun ProgramFragment(
    programItem: Program,
    programProgressItem: ProgramProgress,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = programProgressItem.exercise)
        Text(text = "Planned - ${programItem.reps} reps")
        Slider(
            value = programProgressItem.reps.toFloat(),
            onValueChange = { onValueChange(it) },
            valueRange = 0f..programItem.reps.toFloat()
        )
    }
}