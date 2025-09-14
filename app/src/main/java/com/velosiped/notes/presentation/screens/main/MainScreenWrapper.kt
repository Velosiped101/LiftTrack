package com.velosiped.notes.presentation.screens.main

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.velosiped.notes.StatisticsActivity
import com.velosiped.notes.presentation.navigation.Routes
import com.velosiped.notes.presentation.screens.main.components.training.ConfirmationDialog

@Composable
fun MainScreenWrapper(
    viewModel: MainViewModel,
    navController: NavController
) {
    val uiState = viewModel.uiState.collectAsState().value

    val programChangesFound = viewModel.programChangesFound
    val programResetDone = viewModel.programResetDone

    var dialogIsActive by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        programChangesFound.collect { found ->
            if (found) dialogIsActive = true
            else navController.navigate(Routes.ProgramExec)
        }
    }

    LaunchedEffect(key1 = Unit) {
        programResetDone.collect {
            navController.navigate(Routes.ProgramExec)
        }
    }

    MainScreen(
        targetCalories = uiState.targetCalories,
        currentIntake = uiState.currentIntake,
        trainingState = uiState.trainingState,
        currentGraphData = uiState.currentGraphData,
        checkForProgramUpdate = viewModel::checkForProgramUpdate,
        navigateToNewRecipe = { navController.navigate(Routes.NewRecipe.name) },
        navigateToAddMeal = { navController.navigate(Routes.AddMeal.name) },
        navigateToFoodDbManager = { navController.navigate(Routes.FoodManager.name) },
        navigateToProgramManager = { navController.navigate(Routes.ProgramEdit.name) },
        navigateToSettings = { navController.navigate(Routes.Settings.name) },
        navigateToStatistics = {
            val intent = Intent(context, StatisticsActivity::class.java)
            context.startActivity(intent)
        }
    )
    
    if (dialogIsActive) {
        ConfirmationDialog(
            onDismiss = { dialogIsActive = false },
            onStartNew = { viewModel::resetProgramProgress },
            onContinue = { navController.navigate(Routes.ProgramExec.name) }
        )
    }
}