package com.velosiped.notes.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.main.components.training.ConfirmationDialog
import com.velosiped.notes.presentation.screens.main.components.DietCard
import com.velosiped.notes.presentation.screens.main.components.diet.DietDialog
import com.velosiped.notes.presentation.screens.main.components.ProgramCard
import com.velosiped.notes.presentation.screens.main.components.SettingsCard
import com.velosiped.notes.presentation.screens.main.components.training.ProgramDialog
import com.velosiped.notes.ui.theme.CustomTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun MainScreen(
    uiState: MainScreenUiState,
    uiAction: (MainScreenUiAction) -> Unit,
    changesFound: SharedFlow<Boolean>,
    navigateToNewRecipe: () -> Unit,
    navigateToAddMeal: () -> Unit,
    navigateToFoodDbManager: () -> Unit,
    navigateToProgramManager: () -> Unit,
    navigateToProgramExec: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToStatistics: () -> Unit
) {
    var confirmationDialogIsActive by remember { mutableStateOf(false) }
    var programDialogIsActive by remember { mutableStateOf(false) }
    var dietDialogIsActive by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = changesFound) {
        changesFound.collect { found ->
            if (found) confirmationDialogIsActive = true
            else navigateToProgramExec()
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_8)),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surface)
            .padding(dimensionResource(R.dimen.space_by_8))
    ) {
        DietCard(
            currentCals = uiState.totalCals,
            targetCals = uiState.targetCalories,
            totalProtein = uiState.totalProtein.toInt(),
            totalFat = uiState.totalFat.toInt(),
            totalCarbs = uiState.totalCarbs.toInt(),
            onClick = { dietDialogIsActive = true },
            modifier = Modifier.fillMaxWidth()
        )
        ProgramCard(
            currentProgress = uiState.dayProgress,
            currentGraphData = uiState.currentGraphData,
            onStartTrainingClicked = { uiAction(MainScreenUiAction.CheckForProgramUpdate) },
            onClick = { programDialogIsActive = true },
            modifier = Modifier.fillMaxWidth()
        )
        SettingsCard(
            onClick = navigateToSettings,
            modifier = Modifier.fillMaxWidth()
        )
    }
    if (confirmationDialogIsActive) {
        ConfirmationDialog(
            onDismiss = { confirmationDialogIsActive = false },
            onStartNew = {
                uiAction(MainScreenUiAction.ResetProgramProgress)
                navigateToProgramExec()
            },
            onContinue = {
                navigateToProgramExec()
            }
        )
    }
    if (programDialogIsActive) {
        ProgramDialog(
            onDismiss = { programDialogIsActive = false },
            onProgramManager = { navigateToProgramManager() },
            onStatistics = {
                programDialogIsActive = false
                navigateToStatistics()
            }
        )
    }
    if (dietDialogIsActive) {
        DietDialog(
            onDismiss = { dietDialogIsActive = false },
            onCreateNewRecipe = navigateToNewRecipe,
            onAddMeal = navigateToAddMeal,
            onManageLocalFoodDb = navigateToFoodDbManager
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    CustomTheme {
        MainScreen(
            uiState = MainScreenUiState(),
            uiAction = {},
            changesFound = MutableSharedFlow(),
            navigateToNewRecipe = { },
            navigateToAddMeal = { },
            navigateToFoodDbManager = { },
            navigateToProgramManager = { },
            navigateToProgramExec = { },
            navigateToSettings = { },
            navigateToStatistics = { },
        )
    }
}