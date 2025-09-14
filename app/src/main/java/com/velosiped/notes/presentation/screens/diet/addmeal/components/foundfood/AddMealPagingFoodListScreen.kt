package com.velosiped.notes.presentation.screens.diet.addmeal.components.foundfood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import com.velosiped.notes.R
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.presentation.screens.components.PagingListErrorIndicator
import com.velosiped.notes.presentation.screens.components.PagingListLoadingIndicator
import com.velosiped.notes.presentation.screens.components.fooditem.FoodItemCard
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun AddMealPagingFoodListScreen(
    items: List<Food>,
    loadState: LoadState,
    onItemClicked: (Food) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
        modifier = modifier.fillMaxSize()
    ) {
        items(items) { food ->
            FoodItemCard(
                food = food,
                onFoodItemClicked = onItemClicked,
                onLongClick = { }
            )
        }
        item {
            when (loadState) {
                is LoadState.Error -> PagingListErrorIndicator(
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth()
                )
                LoadState.Loading -> PagingListLoadingIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
                is LoadState.NotLoading -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingMoreScreenPreview() {
    CustomTheme {
        AddMealPagingFoodListScreen(
            items = listOf(
                Food(
                    name = "1",
                    protein = 20.0,
                    fat = 10.0,
                    carbs = 30.0,
                    imageUrl = null
                ),
                Food(
                    name = "2",
                    protein = 10.0,
                    fat = 8.0,
                    carbs = 64.0,
                    imageUrl = null
                ),
                Food(
                    name = "3",
                    protein = 50.0,
                    fat = 5.0,
                    carbs = 10.0,
                    imageUrl = null
                )
            ),
            loadState = LoadState.Loading,
            onItemClicked = {  },
            onRetry = {  }
        )
    }
}