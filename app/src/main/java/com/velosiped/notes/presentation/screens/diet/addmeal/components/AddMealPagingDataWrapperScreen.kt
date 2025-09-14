package com.velosiped.notes.presentation.screens.diet.addmeal.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import com.velosiped.notes.R
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.presentation.screens.diet.addmeal.components.error.AddMealErrorScreen
import com.velosiped.notes.presentation.screens.diet.addmeal.components.foundfood.AddMealPagingFoodListScreen
import com.velosiped.notes.presentation.screens.diet.addmeal.components.foundnothing.AddMealFoundNothingScreen
import com.velosiped.notes.presentation.screens.diet.addmeal.components.loading.AddMealLoadingScreen
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.toHttpError
import com.velosiped.notes.utils.toIOError
import retrofit2.HttpException
import java.io.IOException

@Composable
fun AddMealPagingDataWrapperScreen(
    foodList: List<Food>,
    refreshLoadState: LoadState,
    appendLoadState: LoadState,
    onItemClicked: (Food) -> Unit,
    onRetry: () -> Unit
) {
    when (refreshLoadState) {
        is LoadState.Error -> {
            val rawError = refreshLoadState.error
            val error = when (rawError) {
                is HttpException -> stringResource(id = rawError.toHttpError().messageId)
                is IOException -> stringResource(id = rawError.toIOError().messageId)
                else -> stringResource(id = R.string.unknown_error)
            }
            AddMealErrorScreen(error = error, onRetry = onRetry)
        }
        is LoadState.Loading -> {
            AddMealLoadingScreen()
        }
        is LoadState.NotLoading -> {
            if (foodList.isNotEmpty()) {
                AddMealPagingFoodListScreen(
                    items = foodList,
                    loadState = appendLoadState,
                    onItemClicked = onItemClicked,
                    onRetry = onRetry,
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.space_by_4),
                        end = dimensionResource(R.dimen.space_by_4)
                    )
                )
            } else {
                AddMealFoundNothingScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FinishedLoading() {
    val list = listOf(
        Food(name = "1", protein = 20.0, fat = 10.0, carbs = 30.0, imageUrl = null),
        Food(name = "2", protein = 10.0, fat = 8.0, carbs = 64.0, imageUrl = null),
        Food(name = "3", protein = 50.0, fat = 5.0, carbs = 10.0, imageUrl = null),
        Food(name = "1", protein = 20.0, fat = 10.0, carbs = 30.0, imageUrl = null),
        Food(name = "2", protein = 10.0, fat = 8.0, carbs = 64.0, imageUrl = null),
        Food(name = "3", protein = 50.0, fat = 5.0, carbs = 10.0, imageUrl = null),
        Food(name = "1", protein = 20.0, fat = 10.0, carbs = 30.0, imageUrl = null),
        Food(name = "2", protein = 10.0, fat = 8.0, carbs = 64.0, imageUrl = null),
        Food(name = "3", protein = 50.0, fat = 5.0, carbs = 10.0, imageUrl = null),
    )
    CustomTheme {
        AddMealPagingDataWrapperScreen(
            foodList = list,
            refreshLoadState = LoadState.NotLoading(endOfPaginationReached = true),
            appendLoadState = LoadState.NotLoading(endOfPaginationReached = true),
            onItemClicked = { },
            onRetry = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingNextPage() {
    val list = listOf(
        Food(name = "1", protein = 20.0, fat = 10.0, carbs = 30.0, imageUrl = null),
        Food(name = "2", protein = 10.0, fat = 8.0, carbs = 64.0, imageUrl = null),
        Food(name = "3", protein = 50.0, fat = 5.0, carbs = 10.0, imageUrl = null),
        Food(name = "1", protein = 20.0, fat = 10.0, carbs = 30.0, imageUrl = null),
        Food(name = "2", protein = 10.0, fat = 8.0, carbs = 64.0, imageUrl = null),
        Food(name = "3", protein = 50.0, fat = 5.0, carbs = 10.0, imageUrl = null),
        Food(name = "1", protein = 20.0, fat = 10.0, carbs = 30.0, imageUrl = null),
        Food(name = "2", protein = 10.0, fat = 8.0, carbs = 64.0, imageUrl = null),
        Food(name = "3", protein = 50.0, fat = 5.0, carbs = 10.0, imageUrl = null),
    )
    CustomTheme {
        AddMealPagingDataWrapperScreen(
            foodList = list,
            refreshLoadState = LoadState.NotLoading(endOfPaginationReached = true),
            appendLoadState = LoadState.Loading,
            onItemClicked = { },
            onRetry = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FoundNothing() {
    CustomTheme {
        AddMealPagingDataWrapperScreen(
            foodList = emptyList(),
            refreshLoadState = LoadState.NotLoading(endOfPaginationReached = true),
            appendLoadState = LoadState.NotLoading(endOfPaginationReached = true),
            onItemClicked = { },
            onRetry = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorLoadingNextPage() {
    val list = listOf(
        Food(name = "1", protein = 20.0, fat = 10.0, carbs = 30.0, imageUrl = null),
        Food(name = "2", protein = 10.0, fat = 8.0, carbs = 64.0, imageUrl = null),
        Food(name = "3", protein = 50.0, fat = 5.0, carbs = 10.0, imageUrl = null),
        Food(name = "1", protein = 20.0, fat = 10.0, carbs = 30.0, imageUrl = null),
        Food(name = "2", protein = 10.0, fat = 8.0, carbs = 64.0, imageUrl = null),
        Food(name = "3", protein = 50.0, fat = 5.0, carbs = 10.0, imageUrl = null),
        Food(name = "1", protein = 20.0, fat = 10.0, carbs = 30.0, imageUrl = null),
        Food(name = "2", protein = 10.0, fat = 8.0, carbs = 64.0, imageUrl = null),
        Food(name = "3", protein = 50.0, fat = 5.0, carbs = 10.0, imageUrl = null),
    )
    CustomTheme {
        AddMealPagingDataWrapperScreen(
            foodList = list,
            refreshLoadState = LoadState.NotLoading(endOfPaginationReached = true),
            appendLoadState = LoadState.Error(Throwable("error")),
            onItemClicked = { },
            onRetry = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorLoading() {
    CustomTheme {
        AddMealPagingDataWrapperScreen(
            foodList = emptyList(),
            refreshLoadState = LoadState.Error(Throwable("No internet")),
            appendLoadState = LoadState.NotLoading(endOfPaginationReached = true),
            onItemClicked = { },
            onRetry = { }
        )
    }
}