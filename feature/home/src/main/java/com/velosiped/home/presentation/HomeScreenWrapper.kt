package com.velosiped.home.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.velosiped.home.presentation.components.training.ProgramExecConfirmationPopUp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenWrapper(
    viewModel: HomeViewModel,
    navigateToNewRecipe: () -> Unit,
    navigateToAddMeal: () -> Unit,
    navigateToFoodManager: () -> Unit,
    navigateToProgramExecution: () -> Unit,
    navigateToProgramManager: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToStatistics: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    val programChangesFound = viewModel.programChangesFound
    val programResetDone = viewModel.programResetDone

    var dialogIsActive by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        programChangesFound.collect { found ->
            if (found) dialogIsActive = true
            else navigateToProgramExecution()
        }
    }

    LaunchedEffect(key1 = Unit) {
        programResetDone.collect {
            navigateToProgramExecution()
        }
    }

    HomeScreen(
        targetCalories = uiState.targetCalories,
        currentIntake = uiState.currentIntake,
        trainingState = uiState.trainingState,
        currentGraphData = uiState.currentGraphData,
        checkForProgramUpdate = viewModel::checkForProgramUpdate,
        navigateToNewRecipe = { navigateToNewRecipe() },
        navigateToAddMeal = { navigateToAddMeal() },
        navigateToFoodDbManager = { navigateToFoodManager() },
        navigateToProgramManager = { navigateToProgramManager() },
        navigateToSettings = { navigateToSettings() },
        navigateToStatistics = { navigateToStatistics() }
    )
    
    if (dialogIsActive) {
        BasicAlertDialog(
            onDismissRequest = { dialogIsActive = false }
        ) {
            ProgramExecConfirmationPopUp(
                onStartNew = viewModel::resetProgramProgress,
                onContinue = { navigateToProgramExecution() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}