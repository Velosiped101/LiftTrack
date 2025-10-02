package com.velosiped.addmeal.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.velosiped.addmeal.presentation.components.error.AddMealErrorScreen
import com.velosiped.addmeal.presentation.components.foundnothing.AddMealFoundNothingScreen
import com.velosiped.addmeal.presentation.components.loading.AddMealLoadingScreen
import com.velosiped.ui.R as coreR
import com.velosiped.ui.components.fooditem.FoodItemCard
import com.velosiped.ui.components.paginglist.PagingListErrorIndicator
import com.velosiped.ui.components.paginglist.PagingListLoadingIndicator
import com.velosiped.diet.food.repository.Food

@Composable
fun AddMealPagingDataWrapperScreen(
    pagingItems: LazyPagingItems<Food>,
    onItemClicked: (Food) -> Unit,
    onRetry: () -> Unit
) {
    when (pagingItems.loadState.refresh) {
        is LoadState.Error -> {
            val rawError = (pagingItems.loadState.refresh as LoadState.Error).error
            AddMealErrorScreen(error = rawError.message.toString(), onRetry = onRetry)
        }
        is LoadState.Loading -> {
            AddMealLoadingScreen()
        }
        is LoadState.NotLoading -> {
            if (pagingItems.itemSnapshotList.items.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_8)),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(coreR.dimen.space_by_8))
                ) {
                    items(pagingItems.itemCount) { index ->
                        val food = pagingItems[index]
                        food?.let {
                            FoodItemCard(
                                name = it.name,
                                protein = it.protein,
                                fat = it.fat,
                                carbs = it.carbs,
                                imageUri = it.imageUri,
                                onFoodItemClicked = { onItemClicked(it) },
                                onLongClick = { },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    item {
                        when (pagingItems.loadState.append) {
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
            } else {
                AddMealFoundNothingScreen()
            }
        }
    }
}