package com.velosiped.notes.presentation.screens.diet.addmeal.components.pickedfood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.HUNDRED
import com.velosiped.notes.utils.NutrientsIntake
import com.velosiped.notes.utils.ONE

@Composable
fun AddMealPickedFoodListScreen(
    currentIntake: NutrientsIntake,
    targetCalories: Int,
    pickedFoodMap: Map<Food, Int>,
    onItemDelete: (Food) -> Unit,
    onItemEdit: (Food) -> Unit,
    modifier: Modifier = Modifier
) {
    val pickedFoodIntake = NutrientsIntake(
        protein = pickedFoodMap.run { entries.sumOf { it.key.protein * it.value / Int.HUNDRED }.toFloat() },
        fat = pickedFoodMap.entries.sumOf { it.key.fat * it.value / Int.HUNDRED }.toFloat(),
        carbs = pickedFoodMap.entries.sumOf { it.key.carbs * it.value / Int.HUNDRED }.toFloat()
    )
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
            modifier = Modifier
                .fillMaxWidth()
                .weight(Float.ONE)
        ) {
            items(pickedFoodMap.entries.toList()) {
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
            pickedFoodIntake = pickedFoodIntake,
            targetCalories = targetCalories,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.space_by_4))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        AddMealPickedFoodListScreen(
            currentIntake = NutrientsIntake(
                protein = 40f,
                fat = 20f,
                carbs = 100f
            ),
            targetCalories = 2500,
            pickedFoodMap = mapOf(
                Food(
                    name = "Food 1",
                    protein = 15.0,
                    fat = 5.6,
                    carbs = 34.0,
                    imageUrl = null
                ) to 200,
                Food(
                    name = "Food 2",
                    protein = 30.0,
                    fat = 10.0,
                    carbs = 50.0,
                    imageUrl = null
                ) to 100,
                Food(
                    name = "Food 5",
                    protein = 36.0,
                    fat = 15.8,
                    carbs = 10.0,
                    imageUrl = null
                ) to 300
            ),
            onItemDelete = { },
            onItemEdit = { },
        )
    }
}