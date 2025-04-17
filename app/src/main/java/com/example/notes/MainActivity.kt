package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.notes.presentation.screens.diet.addMealScreen.AddMealViewModel
import com.example.notes.presentation.navigation.Navigation
import com.example.notes.presentation.screens.diet.foodManagerScreen.FoodManagerViewModel
import com.example.notes.presentation.screens.diet.newRecipeScreen.NewRecipeViewModel
import com.example.notes.presentation.screens.mainScreen.MainViewModel
import com.example.notes.presentation.screens.training.programEditScreen.ProgramEditViewModel
import com.example.notes.presentation.screens.training.programExecScreen.ProgramExecViewModel
import com.example.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val addMealViewModel = viewModel(modelClass = AddMealViewModel::class.java)
            val foodManagerViewModel = viewModel(modelClass = FoodManagerViewModel::class.java)
            val mainViewModel = viewModel(modelClass = MainViewModel::class.java)
            val programEditViewModel = viewModel(modelClass = ProgramEditViewModel::class.java)
            val newRecipeViewModel = viewModel(modelClass = NewRecipeViewModel::class.java)
            val programExecViewModel = viewModel(modelClass = ProgramExecViewModel::class.java)
            NotesTheme {
                Navigation(
                    navController = navController,
                    addMealUiState = addMealViewModel.uiState.collectAsState().value,
                    addMealUiActions = { addMealViewModel.uiAction(it) },
                    foodManagerUiState = foodManagerViewModel.uiState.collectAsState().value,
                    foodManagerUiActions = { foodManagerViewModel.uiActions(it) },
                    newRecipeUiState = newRecipeViewModel.uiState.collectAsState().value,
                    newRecipeUiActions = { newRecipeViewModel.uiActions(it) },
                    programEditUiAction = { programEditViewModel.uiAction(it) },
                    programEditUiState = programEditViewModel.uiState.collectAsState().value,
                    programExecUiAction = { programExecViewModel.uiAction(it) },
                    programExecUiState = programExecViewModel.uiState.collectAsState().value,
                    mainScreenUiState = mainViewModel.uiState.collectAsState().value,
                    mainScreenUiAction = { mainViewModel.uiAction(it) }
                )
            }
        }
    }
}