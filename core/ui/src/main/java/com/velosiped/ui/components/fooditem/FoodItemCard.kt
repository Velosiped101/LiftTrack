package com.velosiped.ui.components.fooditem

import android.net.Uri
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
import com.velosiped.ui.R
import com.velosiped.ui.components.fooditem.nutrientsrow.NutrientsRow
import com.velosiped.ui.components.image.FoodImage
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodItemCard(
    name: String,
    protein: Float,
    fat: Float,
    carbs: Float,
    imageUri: Uri?,
    onFoodItemClicked: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    markedForDelete: Boolean = false
) {
    val containerColor =
        if (markedForDelete) CustomTheme.colors.boxCardColors.markedForDeleteColor
        else CustomTheme.colors.boxCardColors.containerColor

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.box_card_corner_radius)))
            .combinedClickable(
                onLongClick = onLongClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onFoodItemClicked() }
            .background(containerColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.space_by_4))
        ) {
            FoodImage(
                uri = imageUri,
                modifier = Modifier.size(dimensionResource(R.dimen.food_list_card_picture_size))
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_8))
            ) {
                Text(
                    text = name,
                    style = CustomTheme.typography.screenMessageMedium,
                    maxLines = Int.ONE,
                    overflow = TextOverflow.Ellipsis,
                    color = CustomTheme.colors.primaryTextColor
                )
                NutrientsRow(
                    protein = protein,
                    fat = fat,
                    carbs = carbs,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 5)
@Composable
private fun NormalPreview() {
    CustomTheme {
        FoodItemCard(
            name = "Apple pie",
            protein = 36f,
            fat = 5f,
            carbs = 45f,
            imageUri = null,
            onFoodItemClicked = { },
            onLongClick = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MarkedForDeletePreview() {
    CustomTheme {
        FoodItemCard(
            name = "Apple pie",
            protein = 36f,
            fat = 5f,
            carbs = 45f,
            imageUri = null,
            onFoodItemClicked = { },
            onLongClick = { },
            markedForDelete = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}