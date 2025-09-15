package com.velosiped.notes.presentation.screens.diet.addmeal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.paging.compose.collectAsLazyPagingItems
import com.velosiped.notes.utils.EMPTY

@Composable
fun AddMealScreenWrapper(
    viewModel: AddMealViewModel,
    navigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    val saveCompleted = viewModel.saveCompleted
    val pagingDataFlow = uiState.pagingDataFlow?.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        saveCompleted.collect {
            navigateBack()
        }
    }

    AddMealScreen(
        foodFoundList = pagingDataFlow?.itemSnapshotList?.items,
        refreshLoadState = pagingDataFlow?.loadState?.refresh,
        appendLoadState = pagingDataFlow?.loadState?.append,
        searchBarQuery = uiState.searchBarQuery,
        searchMode = uiState.searchMode,
        currentIntake = uiState.currentIntake,
        selectedFoodMap = uiState.selectedFoodMap,
        targetCalories = uiState.targetCalories,
        selectedFoodMass = uiState.selectedFoodMass,
        removeFoodFromSelectedMap = viewModel::removeFoodFromPickedList,
        selectFood = viewModel::onFoodPicked,
        confirmMeal = viewModel::confirmMealAddition,
        changeSearchQuery = viewModel::searchBarTextChanged,
        changeSearchMode = viewModel::setSearchMode,
        clearSearchQuery = viewModel::clearSearchBarInput,
        changeSelectedFoodMass = viewModel::onMassInputChanged,
        insertSelectedFoodToMap = viewModel::addFoodToPickedList,
        retrySearch = { pagingDataFlow?.retry() },
        navigateBack = { navigateBack() }
    )
}