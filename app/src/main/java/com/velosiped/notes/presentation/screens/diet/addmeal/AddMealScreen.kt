package com.velosiped.notes.presentation.screens.diet.addmeal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import com.velosiped.notes.R
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.presentation.screens.diet.addmeal.components.AddMealPagingDataWrapperScreen
import com.velosiped.notes.presentation.screens.diet.addmeal.components.AddMealTopBar
import com.velosiped.notes.presentation.screens.diet.addmeal.components.MassInputDialog
import com.velosiped.notes.presentation.screens.diet.addmeal.components.SearchModeSwitcher
import com.velosiped.notes.presentation.screens.diet.addmeal.components.initial.AddMealStartScreen
import com.velosiped.notes.presentation.screens.diet.addmeal.components.pickedfood.AddMealPickedFoodListScreen
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.NutrientsIntake
import com.velosiped.notes.utils.SearchMode

@Composable
fun AddMealScreen(
    foodFoundList: List<Food>?,
    refreshLoadState: LoadState?,
    appendLoadState: LoadState?,
    searchBarQuery: String,
    searchMode: SearchMode,
    currentIntake: NutrientsIntake,
    selectedFoodMap: Map<Food, Int>,
    targetCalories: Int,
    selectedFoodMass: String,
    changeSelectedFoodMass: (String) -> Unit,
    insertSelectedFoodToMap: () -> Unit,
    removeFoodFromSelectedMap: (Food) -> Unit,
    selectFood: (Food) -> Unit,
    confirmMeal: () -> Unit,
    changeSearchQuery: (String) -> Unit,
    changeSearchMode: (SearchMode) -> Unit,
    clearSearchQuery: () -> Unit,
    retrySearch: () -> Unit,
    navigateBack: () -> Unit
) {
    var massDialogIsActive by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            AddMealTopBar(
                searchInput = searchBarQuery,
                saveButtonEnabled = selectedFoodMap.isNotEmpty(),
                onInputClear = clearSearchQuery,
                onInputChange = changeSearchQuery,
                onSave = confirmMeal,
                onNavigateBack = navigateBack
            )
        },
        containerColor = CustomTheme.colors.mainBackgroundColor,
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            SearchModeSwitcher(
                searchMode = searchMode,
                onModeSelected = changeSearchMode,
                modifier = Modifier.fillMaxWidth()
            )
            if (searchBarQuery.isNotBlank()) {
                if (refreshLoadState != null && appendLoadState != null && foodFoundList != null) {
                    AddMealPagingDataWrapperScreen(
                        foodList = foodFoundList,
                        refreshLoadState = refreshLoadState,
                        appendLoadState = appendLoadState,
                        onItemClicked = {
                            selectFood(it)
                            massDialogIsActive = true
                        },
                        onRetry = retrySearch
                    )
                }
            } else {
                if (selectedFoodMap.isEmpty()) {
                    AddMealStartScreen()
                }
                else {
                    AddMealPickedFoodListScreen(
                        currentIntake = currentIntake,
                        targetCalories = targetCalories,
                        pickedFoodMap = selectedFoodMap,
                        onItemDelete = removeFoodFromSelectedMap,
                        onItemEdit = {
                            selectFood(it)
                            massDialogIsActive = true
                        },
                        modifier = Modifier.padding(dimensionResource(R.dimen.space_by_8))
                    )
                }
            }
        }
    }
    if (massDialogIsActive) {
        MassInputDialog(
            value = selectedFoodMass,
            onValueChange = changeSelectedFoodMass,
            onConfirm = insertSelectedFoodToMap,
            onDismiss = { massDialogIsActive = false }
        )
    }
}

@Preview
@Composable
private fun FoundFood() {
    val list = listOf(
        Food(name = "Fried rice with shrimp", protein = 20.0, fat = 10.0, carbs = 30.0, imageUrl = null),
        Food(name = "White rice", protein = 10.0, fat = 8.0, carbs = 64.0, imageUrl = null),
        Food(name = "Rice with tomatoes", protein = 50.0, fat = 5.0, carbs = 10.0, imageUrl = null)
    )
    AddMealScreen(
        foodFoundList = list,
        refreshLoadState = LoadState.NotLoading(endOfPaginationReached = true),
        appendLoadState = LoadState.NotLoading(endOfPaginationReached = true),
        searchBarQuery = "Rice",
        searchMode = SearchMode.Remote,
        currentIntake = NutrientsIntake(protein = 10f, fat = 5f, carbs = 40f),
        selectedFoodMap = emptyMap(),
        targetCalories = 2500,
        removeFoodFromSelectedMap = { },
        selectFood = { },
        confirmMeal = { },
        changeSearchQuery = { },
        changeSearchMode = { },
        clearSearchQuery = { },
        retrySearch = { },
        navigateBack = { },
        selectedFoodMass = "400",
        changeSelectedFoodMass = {  },
        insertSelectedFoodToMap = {  }
    )
}