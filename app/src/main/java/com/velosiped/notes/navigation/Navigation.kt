package com.velosiped.notes.navigation

import android.content.Intent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.velosiped.addmeal.presentation.AddMealScreenWrapper
import com.velosiped.addmeal.presentation.AddMealViewModel
import com.velosiped.foodmanager.presentation.FoodManagerScreenWrapper
import com.velosiped.foodmanager.presentation.FoodManagerViewModel
import com.velosiped.home.presentation.HomeScreenWrapper
import com.velosiped.home.presentation.HomeViewModel
import com.velosiped.newrecipe.presentation.NewRecipeScreenWrapper
import com.velosiped.newrecipe.presentation.NewRecipeViewModel
import com.velosiped.notes.activities.StatisticsActivity
import com.velosiped.notes.utils.IntentExtras
import com.velosiped.programexecution.presentation.ProgramExecScreenWrapper
import com.velosiped.programexecution.presentation.ProgramExecViewModel
import com.velosiped.programmanager.presentation.ProgramEditScreenWrapper
import com.velosiped.programmanager.presentation.ProgramEditViewModel
import com.velosiped.settings.presentation.SettingsScreenWrapper
import com.velosiped.settings.presentation.SettingsViewModel
import com.velosiped.splash.presentation.CustomSplashScreen
import com.velosiped.utility.extensions.ZERO

@Composable
fun Navigation(
    settingsViewModel: SettingsViewModel,
    homeViewModel: HomeViewModel,
    isNotFirstLaunch: Boolean?,
    isDarkTheme: Boolean,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(Routes.SPLASH.name) {
            CustomSplashScreen(
                isNotFirstLaunch = isNotFirstLaunch,
                onNavigateToMain = {
                    navController.navigate(Routes.HOME.name) { popUpTo(Int.ZERO) { inclusive = true } }
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS.name) { popUpTo(Int.ZERO) { inclusive = true } }
                }
            )
        }
        composable(Routes.HOME.name) {
            val context = LocalContext.current
            HomeScreenWrapper(
                viewModel = homeViewModel,
                navigateToNewRecipe = { navController.navigate(Routes.NEW_RECIPE.name) },
                navigateToAddMeal = { navController.navigate(Routes.ADD_MEAL.name) },
                navigateToFoodManager = { navController.navigate(Routes.FOOD_MANAGER.name) },
                navigateToProgramExecution = { navController.navigate(Routes.PROGRAM_EXEC.name) },
                navigateToProgramManager = { navController.navigate(Routes.PROGRAM_MANAGER.name) },
                navigateToSettings = { navController.navigate(Routes.SETTINGS.name) },
                navigateToStatistics = {
                    val intent = Intent(context, StatisticsActivity::class.java)
                        .putExtra(IntentExtras.INTENT_EXTRA_DARK_THEME, isDarkTheme)
                    context.startActivity(intent)
                }
            )
        }
        composable(Routes.ADD_MEAL.name) {
            val viewModel = hiltViewModel<AddMealViewModel>()
            AddMealScreenWrapper(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.FOOD_MANAGER.name) {
            val viewModel = hiltViewModel<FoodManagerViewModel>()
            FoodManagerScreenWrapper(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PROGRAM_MANAGER.name) {
            val viewModel = hiltViewModel<ProgramEditViewModel>()
            ProgramEditScreenWrapper(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PROGRAM_EXEC.name) {
            val viewModel = hiltViewModel<ProgramExecViewModel>()
            ProgramExecScreenWrapper(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.NEW_RECIPE.name) {
            val viewModel = hiltViewModel<NewRecipeViewModel>()
            NewRecipeScreenWrapper(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.SETTINGS.name) {
            SettingsScreenWrapper(
                viewModel = settingsViewModel,
                onNavigateToMainScreen = {
                    navController.navigate(Routes.HOME.name) { popUpTo(Int.ZERO) { inclusive = true } }
                }
            )
        }
    }
}