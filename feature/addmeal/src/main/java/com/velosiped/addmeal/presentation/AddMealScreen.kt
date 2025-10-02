package com.velosiped.addmeal.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.velosiped.addmeal.presentation.components.AddMealPagingDataWrapperScreen
import com.velosiped.addmeal.presentation.components.AddMealTopBar
import com.velosiped.addmeal.presentation.components.MassInputPopUp
import com.velosiped.addmeal.presentation.components.SearchModeSwitcher
import com.velosiped.addmeal.presentation.components.initial.AddMealStartScreen
import com.velosiped.addmeal.presentation.components.pickedfood.AddMealPickedFoodListScreen
import com.velosiped.addmeal.presentation.utils.SearchMode
import com.velosiped.ui.R
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.diet.food.repository.Food
import com.velosiped.utility.data.NutrientsIntake
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(
    pagingDataFlow: Flow<PagingData<Food>>,
    searchBarState: TextFieldState,
    massInputState: TextFieldState,
    searchMode: SearchMode,
    currentIntake: NutrientsIntake,
    selectedFoodIntake: NutrientsIntake,
    selectedFoodMap: Map<Food, Int>,
    targetCalories: Int,
    onFoodClick: (Food) -> Unit,
    onMassInputChange: (String) -> Unit,
    onSearchInputChange: (String) -> Unit,
    onConfirmMeal: () -> Unit,
    onDeleteFromSelectedMap: (Food) -> Unit,
    onConfirmAddingToSelectedMap: () -> Unit,
    onSearchModeChange: (SearchMode) -> Unit,
    onSearchInputClear: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val pagingData = pagingDataFlow.collectAsLazyPagingItems()

    var massDialogIsActive by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            AddMealTopBar(
                searchInput = searchBarState.text,
                saveButtonEnabled = selectedFoodMap.isNotEmpty(),
                onInputClear = onSearchInputClear,
                onInputChange = onSearchInputChange,
                onSave = onConfirmMeal,
                onNavigateBack = onNavigateBack
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
                onModeSelected = onSearchModeChange,
                modifier = Modifier.fillMaxWidth()
            )
            if (searchBarState.text.isNotBlank()) {
                AddMealPagingDataWrapperScreen(
                    pagingItems = pagingData,
                    onItemClicked = {
                        onFoodClick(it)
                        massDialogIsActive = true
                    },
                    onRetry = { pagingData.retry() }
                )
            } else {
                if (selectedFoodMap.isEmpty()) {
                    AddMealStartScreen()
                } else {
                    AddMealPickedFoodListScreen(
                        currentIntake = currentIntake,
                        selectedFoodIntake = selectedFoodIntake,
                        targetCalories = targetCalories,
                        selectedFoodMap = selectedFoodMap,
                        onItemDelete = onDeleteFromSelectedMap,
                        onItemEdit = {
                            onFoodClick(it)
                            massDialogIsActive = true
                        },
                        modifier = Modifier.padding(dimensionResource(R.dimen.space_by_8))
                    )
                }
            }
        }
    }
    if (massDialogIsActive) {
        BasicAlertDialog(
            onDismissRequest = { massDialogIsActive = false }
        ) {
            MassInputPopUp(
                inputState = massInputState,
                onValueChange = onMassInputChange,
                onConfirm = {
                    onConfirmAddingToSelectedMap()
                    massDialogIsActive = false
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun FoundFood() {
    val list = listOf(
        Food(name = "Fried rice with shrimp", protein = 20f, fat = 10f, carbs = 30f, imageUri = null),
        Food(name = "White rice", protein = 10f, fat = 8f, carbs = 64f, imageUri = null),
        Food(name = "Rice with tomatoes", protein = 50f, fat = 5f, carbs = 10f, imageUri = null)
    )
    AddMealScreen(
        pagingDataFlow = MutableStateFlow(PagingData.from(list)),
        searchBarState = TextFieldState(text = "Rice"),
        searchMode = SearchMode.Remote,
        currentIntake = NutrientsIntake(protein = 10f, fat = 5f, carbs = 40f),
        selectedFoodIntake = NutrientsIntake(protein = 20f, fat = 10f, carbs = 10f),
        selectedFoodMap = emptyMap(),
        targetCalories = 2500,
        massInputState = TextFieldState("200"),
        onFoodClick = {  },
        onMassInputChange = {  },
        onSearchInputChange = {  },
        onConfirmMeal = {  },
        onDeleteFromSelectedMap = {  },
        onConfirmAddingToSelectedMap = {  },
        onSearchModeChange = {  },
        onSearchInputClear = {  },
        onNavigateBack = {  },
    )
}