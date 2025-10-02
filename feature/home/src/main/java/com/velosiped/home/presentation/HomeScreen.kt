package com.velosiped.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.home.presentation.components.DietCard
import com.velosiped.home.presentation.components.ProgramCard
import com.velosiped.home.presentation.components.SettingsCard
import com.velosiped.home.presentation.components.diet.DietPopUp
import com.velosiped.home.presentation.components.training.ProgramPopUp
import com.velosiped.home.presentation.utils.TrainingState
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.model.graph.GraphData
import com.velosiped.utility.data.NutrientsIntake
import com.velosiped.ui.R as coreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
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
        verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_8)),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(CustomTheme.colors.mainBackgroundColor)
            .padding(dimensionResource(coreR.dimen.space_by_8))
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
        BasicAlertDialog(
            onDismissRequest = { programDialogIsActive = false }
        ) {
            ProgramPopUp(
                onProgramManager = { navigateToProgramManager() },
                onStatistics = {
                    programDialogIsActive = false
                    navigateToStatistics()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    if (dietDialogIsActive) {
        BasicAlertDialog(
            onDismissRequest = { dietDialogIsActive = false }
        ) {
            DietPopUp(
                onCreateNewRecipe = navigateToNewRecipe,
                onAddMeal = navigateToAddMeal,
                onManageLocalFoodDb = navigateToFoodDbManager,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    CustomTheme {
        HomeScreen(
            targetCalories = 2500,
            currentIntake = NutrientsIntake(
                protein = 60f,
                fat = 45f,
                carbs = 180f
            ),
            trainingState = TrainingState.DONE,
            currentGraphData = GraphData(exercise = "", emptyList()),
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