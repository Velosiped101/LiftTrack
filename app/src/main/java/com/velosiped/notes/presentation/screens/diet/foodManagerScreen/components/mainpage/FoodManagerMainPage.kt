package com.velosiped.notes.presentation.screens.diet.foodManagerScreen.components.mainpage

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
import com.velosiped.notes.R
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.presentation.screens.components.ScreenMessage
import com.velosiped.notes.presentation.screens.components.fooditem.FoodItemCard
import com.velosiped.notes.ui.theme.CustomTheme

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
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.space_by_4))
            ) {
                items(foodList.sortedBy { it.name }) { item ->
                    FoodItemCard(
                        food = item,
                        onFoodItemClicked = { onFoodClicked(item) },
                        onLongClick = { onLongClick(item) },
                        markedForDelete = item in markedForDeleteList,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else {
            ScreenMessage(
                message = stringResource(R.string.empty_food_menu_message),
                painter = painterResource(id = R.drawable.empty_food_menu),
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
    CustomTheme {
        FoodManagerMainPage(
            foodList = listOf(
                Food(name = "Unknown food", protein = 40.0, fat = 25.0, carbs = 36.0, imageUrl = null),
                Food(name = "Something", protein = 60.0, fat = 25.0, carbs = 36.0, imageUrl = null),
                Food(name = "Delete", protein = 80.0, fat = 25.0, carbs = 36.0, imageUrl = null),
                Food(name = "Tomato paste", protein = 10.0, fat = 25.0, carbs = 36.0, imageUrl = null)
            ),
            onFoodClicked = { },
            onLongClick = { },
            markedForDeleteList = listOf(Food(name = "Delete", protein = 80.0, fat = 25.0, carbs = 36.0, imageUrl = null)),
            modifier = Modifier.fillMaxSize()
        )
    }
}