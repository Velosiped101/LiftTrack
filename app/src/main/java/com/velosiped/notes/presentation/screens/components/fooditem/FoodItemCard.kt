package com.velosiped.notes.presentation.screens.components.fooditem

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.velosiped.notes.R
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.ONE

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodItemCard(
    food: Food,
    onFoodItemClicked: (Food) -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    markedForDelete: Boolean = false
) {
    val containerColor = if (markedForDelete) CustomTheme.colors.listItemColors.markedForDeleteColor
    else CustomTheme.colors.listItemColors.containerColor

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.food_list_card_corner_radius)))
            .combinedClickable(
                onLongClick = onLongClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onFoodItemClicked(food) }
            .background(containerColor),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.space_by_4))
        ) {
            FoodImage(
                uri = food.imageUrl?.toUri(),
                modifier = Modifier.size(dimensionResource(R.dimen.food_list_card_picture_size))
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_8))
            ) {
                Text(
                    text = food.name,
                    style = CustomTheme.typography.screenMessageMedium,
                    maxLines = Int.ONE,
                    overflow = TextOverflow.Ellipsis
                )
                NutrientsRow(
                    protein = food.protein.toFloat(),
                    fat = food.fat.toFloat(),
                    carbs = food.carbs.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview()
@Composable
private fun Preview() {
    CustomTheme {
        FoodItemCard(
            food = Food(
                name = "Apple pie",
                protein = 36.0,
                fat = 10.0,
                carbs = 45.0,
                imageUrl = null
            ),
            onFoodItemClicked = { },
            onLongClick = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}