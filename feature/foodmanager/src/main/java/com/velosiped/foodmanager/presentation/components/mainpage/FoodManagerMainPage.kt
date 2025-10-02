package com.velosiped.foodmanager.presentation.components.mainpage

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.diet.food.repository.Food
import com.velosiped.foodmanager.R
import com.velosiped.ui.R as coreR
import com.velosiped.ui.components.ScreenMessage
import com.velosiped.ui.components.fooditem.FoodItemCard
import com.velosiped.ui.theme.CustomTheme

@Composable
fun FoodManagerMainPage(
    foodList: List<Food>,
    markedForDeleteList: List<Food>,
    onFoodClicked: (Food) -> Unit,
    onLongClick: (Food) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = foodList.isNotEmpty(),
        modifier = modifier
    ) { notEmpty ->
        if (notEmpty) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(coreR.dimen.space_by_8))
            ) {
                items(foodList.sortedBy { it.name }) {
                    FoodItemCard(
                        name = it.name,
                        protein = it.protein,
                        fat = it.fat,
                        carbs = it.carbs,
                        imageUri = it.imageUri,
                        onFoodItemClicked = { onFoodClicked(it) },
                        onLongClick = { onLongClick(it) },
                        markedForDelete = it in markedForDeleteList,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else {
            ScreenMessage(
                message = stringResource(R.string.empty_food_menu_message),
                painter = painterResource(id = coreR.drawable.empty_food_menu),
                subtext = stringResource(id = R.string.empty_food_menu_message_hint),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyDatabase() {
    CustomTheme {
        FoodManagerMainPage(
            foodList = emptyList(),
            onFoodClicked = { },
            onLongClick = { },
            markedForDeleteList = emptyList(),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilledDatabase() {
    val list = listOf(
        Food(name = "Unknown food", protein = 40f, fat = 25f, carbs = 36f, imageUri = null),
        Food(name = "Something", protein = 60f, fat = 2f, carbs = 36f, imageUri = null),
        Food(name = "Delete", protein = 80f, fat = 25f, carbs = 36f, imageUri = null),
        Food(name = "Tomato paste", protein = 0f, fat = 0f, carbs = 0f, imageUri = null)
    )
    CustomTheme {
        FoodManagerMainPage(
            foodList = list,
            onFoodClicked = { },
            onLongClick = { },
            markedForDeleteList = listOf(list[1]),
            modifier = Modifier.fillMaxSize()
        )
    }
}