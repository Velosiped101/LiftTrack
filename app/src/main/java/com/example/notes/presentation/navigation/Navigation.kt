package com.example.notes.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.presentation.screens.mainScreen.MainScreen
import com.example.notes.presentation.screens.diet.addMealScreen.AddMealScreen
import com.example.notes.presentation.screens.diet.addMealScreen.AddMealUiAction
import com.example.notes.presentation.screens.diet.addMealScreen.AddMealUiState
import com.example.notes.presentation.screens.diet.foodManagerScreen.FoodManagerScreen
import com.example.notes.presentation.screens.diet.foodManagerScreen.FoodManagerUiAction
import com.example.notes.presentation.screens.diet.foodManagerScreen.FoodManagerUiState
import com.example.notes.presentation.screens.diet.newRecipeScreen.NewRecipeScreen
import com.example.notes.presentation.screens.diet.newRecipeScreen.NewRecipeUiAction
import com.example.notes.presentation.screens.diet.newRecipeScreen.NewRecipeUiState
import com.example.notes.presentation.screens.mainScreen.MainScreenUiAction
import com.example.notes.presentation.screens.mainScreen.MainScreenUiState
import com.example.notes.presentation.screens.training.programEditScreen.ProgramEditScreen
import com.example.notes.presentation.screens.training.programEditScreen.ProgramEditUiAction
import com.example.notes.presentation.screens.training.programEditScreen.ProgramEditUiState
import com.example.notes.presentation.screens.training.programExecScreen.ProgramExecScreen
import com.example.notes.presentation.screens.training.programExecScreen.ProgramExecUiAction
import com.example.notes.presentation.screens.training.programExecScreen.ProgramExecUiState

@Composable
fun Navigation(
    navController: NavHostController,
    addMealUiState: AddMealUiState,
    addMealUiActions: (AddMealUiAction) -> Unit,
    foodManagerUiState: FoodManagerUiState,
    foodManagerUiActions: (FoodManagerUiAction) -> Unit,
    newRecipeUiState: NewRecipeUiState,
    newRecipeUiActions: (NewRecipeUiAction) -> Unit,
    programEditUiState: ProgramEditUiState,
    programEditUiAction: (ProgramEditUiAction) -> Unit,
    programExecUiState: ProgramExecUiState,
    programExecUiAction: (ProgramExecUiAction) -> Unit,
    mainScreenUiState: MainScreenUiState,
    mainScreenUiAction: (MainScreenUiAction) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Main.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(Routes.Main.name) {
            MainScreen(
                uiState = mainScreenUiState,
                uiAction = mainScreenUiAction,
                navigateToNewRecipe = {
                    navController.navigate(Routes.NewRecipe.name)
                },
                navigateToAddMeal = {
                    navController.navigate(Routes.AddMeal.name)
                },
                navigateToFoodDbManager = {
                    navController.navigate(Routes.FoodManager.name)
                },
                navigateToProgramManager = {
                    navController.navigate(Routes.ProgramEdit.name)
                },
                navigateToProgramExec = {
                    navController.navigate(Routes.ProgramExec.name)
                }
            )
        }
        composable(Routes.AddMeal.name) { AddMealScreen(
            uiState = addMealUiState,
            uiActions = addMealUiActions,
            onNavigateBack = { navController.navigateUp() }
        ) }
        composable(Routes.FoodManager.name) {
            FoodManagerScreen(
                uiState = foodManagerUiState,
                uiActions = foodManagerUiActions,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable(Routes.ProgramEdit.name) {
            ProgramEditScreen(
                uiState = programEditUiState,
                uiAction = programEditUiAction,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable(Routes.ProgramExec.name) {
            ProgramExecScreen(
                uiState = programExecUiState,
                uiAction = programExecUiAction
            )
        }
        composable(Routes.NewRecipe.name) {
            NewRecipeScreen(
                uiState = newRecipeUiState,
                uiActions = newRecipeUiActions,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

enum class Routes {
    Main,
    FoodManager,
    AddMeal,
    ProgramEdit,
    ProgramExec,
    NewRecipe
}