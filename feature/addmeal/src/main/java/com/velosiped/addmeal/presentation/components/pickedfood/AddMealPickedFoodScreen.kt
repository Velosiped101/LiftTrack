package com.velosiped.addmeal.presentation.components.pickedfood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R as coreR
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.diet.food.repository.Food
import com.velosiped.utility.data.NutrientsIntake
import com.velosiped.utility.extensions.ONE

@Composable
fun AddMealPickedFoodListScreen(
    currentIntake: NutrientsIntake,
    selectedFoodIntake: NutrientsIntake,
    selectedFoodMap: Map<Food, Int>,
    targetCalories: Int,
    onItemDelete: (Food) -> Unit,
    onItemEdit: (Food) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_8)),
            modifier = Modifier
                .fillMaxWidth()
                .weight(Float.ONE)
        ) {
            items(selectedFoodMap.entries.toList()) {
                PickedFoodItem(
                    name = it.key.name,
                    mass = it.value,
                    onEdit = { onItemEdit(it.key) },
                    onDelete = { onItemDelete(it.key) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        NutrientsProgressBar(
            currentIntake = currentIntake,
            selectedFoodIntake = selectedFoodIntake,
            targetCalories = targetCalories,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        AddMealPickedFoodListScreen(
            currentIntake = NutrientsIntake(protein = 40f, fat = 20f, carbs = 100f),
            selectedFoodIntake = NutrientsIntake(protein = 20f, fat = 30f, carbs = 60f),
            targetCalories = 2500,
            selectedFoodMap = mapOf(
                Food(name = "Food 1", protein = 15f, fat = 5f, carbs = 34f, imageUri = null) to 200,
                Food(name = "Food 2", protein = 30f, fat = 10f, carbs = 50f, imageUri = null) to 100,
                Food(name = "Food 5", protein = 36f, fat = 15f, carbs = 10f, imageUri = null) to 300
            ),
            onItemDelete = { },
            onItemEdit = { }
        )
    }
}