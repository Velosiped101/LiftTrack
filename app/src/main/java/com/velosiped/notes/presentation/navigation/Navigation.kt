package com.velosiped.notes.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.velosiped.notes.presentation.screens.diet.addmeal.AddMealScreen
import com.velosiped.notes.presentation.screens.diet.addmeal.AddMealViewModel
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.FoodManagerScreen
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.FoodManagerViewModel
import com.velosiped.notes.presentation.screens.diet.newRecipeScreen.NewRecipeScreen
import com.velosiped.notes.presentation.screens.diet.newRecipeScreen.NewRecipeViewModel
import com.velosiped.notes.presentation.screens.main.MainScreenWrapper
import com.velosiped.notes.presentation.screens.main.MainViewModel
import com.velosiped.notes.presentation.screens.settingsScreen.SettingsScreen
import com.velosiped.notes.presentation.screens.settingsScreen.SettingsViewModel
import com.velosiped.notes.presentation.screens.splashScreen.SplashScreen
import com.velosiped.notes.presentation.screens.training.programEditScreen.ProgramEditScreen
import com.velosiped.notes.presentation.screens.training.programEditScreen.ProgramEditViewModel
import com.velosiped.notes.presentation.screens.training.programExecScreen.ProgramExecScreen
import com.velosiped.notes.presentation.screens.training.programExecScreen.ProgramExecViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    mainViewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(Routes.Splash.name) {
            SplashScreen(
                viewModel = settingsViewModel,
                onNavigateToMain = {
                    navController.navigate(Routes.Main.name) { popUpTo(0) { inclusive = true } }
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.Settings.name) { popUpTo(0) { inclusive = true } }
                }
            )
        }
        composable(Routes.Main.name) {
            MainScreenWrapper(
                viewModel = mainViewModel,
                navController = navController
            )
        }
        composable(Routes.AddMeal.name) {
            val viewModel = hiltViewModel<AddMealViewModel>()
            val uiState by viewModel.uiState.collectAsState()
//            AddMealScreen(
//                uiState = uiState,
//                uiActions = viewModel::uiAction,
//                saveCompleted = viewModel.saveCompleted,
//                navigateBack = { navController.navigateUp() }
//            )
        }
        composable(Routes.FoodManager.name) {
            val viewModel = hiltViewModel<FoodManagerViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            FoodManagerScreen(
                uiState = uiState,
                uiActions = viewModel::uiActions,
                onNavigateBack = { navController.navigateUp() },
                loadingFinished = viewModel.loadingFinished
            )
        }
        composable(Routes.ProgramEdit.name) {
            val viewModel = hiltViewModel<ProgramEditViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            ProgramEditScreen(
                uiState = uiState,
                uiAction = viewModel::uiAction,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable(Routes.ProgramExec.name) {
            val viewModel = hiltViewModel<ProgramExecViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            ProgramExecScreen(
                uiState = uiState,
                uiAction = viewModel::uiAction,
                saveCompleted = viewModel.saveCompleted,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable(Routes.NewRecipe.name) {
            val viewModel = hiltViewModel<NewRecipeViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            NewRecipeScreen(
                uiState = uiState,
                uiActions = viewModel::uiActions,
                operationCompleted = viewModel.saveCompleted,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable(Routes.Settings.name) {
            val uiState by settingsViewModel.uiState.collectAsState()
            SettingsScreen(
                uiState = uiState,
                uiAction = settingsViewModel::uiAction,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

enum class Routes {
    Splash,
    Main,
    FoodManager,
    AddMeal,
    ProgramEdit,
    ProgramExec,
    NewRecipe,
    Settings
}