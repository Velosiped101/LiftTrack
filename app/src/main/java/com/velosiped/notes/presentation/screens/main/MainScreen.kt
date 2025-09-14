package com.velosiped.notes.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.main.components.DietCard
import com.velosiped.notes.presentation.screens.main.components.diet.DietDialog
import com.velosiped.notes.presentation.screens.main.components.ProgramCard
import com.velosiped.notes.presentation.screens.main.components.SettingsCard
import com.velosiped.notes.presentation.screens.main.components.training.ProgramDialog
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.GraphData
import com.velosiped.notes.utils.NutrientsIntake
import com.velosiped.notes.utils.TrainingState

@Composable
fun MainScreen(
    targetCalories: Int,
    currentIntake: NutrientsIntake,
    trainingState: TrainingState,
    currentGraphData: GraphData,
    checkForProgramUpdate: () -> Unit,
    navigateToNewRecipe: () -> Unit,
    navigateToAddMeal: () -> Unit,
    navigateToFoodDbManager: () -> Unit,
    navigateToProgramManager: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToStatistics: () -> Unit
) {
    var programDialogIsActive by remember { mutableStateOf(false) }
    var dietDialogIsActive by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_8)),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(CustomTheme.colors.mainBackgroundColor)
            .padding(dimensionResource(R.dimen.space_by_8))
    ) {
        DietCard(
            targetCals = targetCalories,
            currentIntake = currentIntake,
            onClick = { dietDialogIsActive = true },
            modifier = Modifier.fillMaxWidth()
        )
        ProgramCard(
            currentTrainingState = trainingState,
            currentGraphData = currentGraphData,
            onStartTrainingClicked = checkForProgramUpdate,
            onClick = { programDialogIsActive = true },
            modifier = Modifier.fillMaxWidth()
        )
        SettingsCard(
            onClick = navigateToSettings,
            modifier = Modifier.fillMaxWidth()
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
            targetCalories = 2500,
            currentIntake = NutrientsIntake(
                protein = 60f,
                fat = 45f,
                carbs = 180f
            ),
            trainingState = TrainingState.TRAINING,
            currentGraphData = GraphData(),
            checkForProgramUpdate = { },
            navigateToNewRecipe = {  },
            navigateToAddMeal = {  },
            navigateToFoodDbManager = {  },
            navigateToProgramManager = {  },
            navigateToSettings = {  },
            navigateToStatistics = {  }
        )
    }
}