package com.velosiped.notes.presentation.screens.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.velosiped.notes.R
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.ui.theme.foodName
import com.velosiped.notes.ui.theme.foodNutrientCount
import com.velosiped.notes.ui.theme.foodNutrientInformation
import com.velosiped.notes.ui.theme.foodNutrientShort

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodItemCard(
    food: Food,
    onFoodItemClicked: () -> Unit,
    onLongClick: () -> Unit,
    colors: CardColors
) {
    val cardShape = RoundedCornerShape(25)
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clip(cardShape)
            .combinedClickable(onLongClick = onLongClick) {
                onFoodItemClicked()
            },
        shape = cardShape,
        colors = colors,
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodImage(food = food)
            Column(modifier = Modifier.padding(end = 8.dp)) {
                FoodName(food = food)
                Spacer(modifier = Modifier.height(8.dp))
                FoodNutrients(food = food)
            }
        }
    }
}

@Composable
private fun FoodImage(
    food: Food
) {
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(25)),
        contentAlignment = Alignment.Center
    ){
        if (food.imageUrl == null) {
            Icon(
                painter = painterResource(id = R.drawable.food_image_placeholder),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(.75f),
                tint = MaterialTheme.colorScheme.onSurface
            )
        } else
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(food.imageUrl)
                        .build(),
                    onLoading = { isLoading = true },
                    onSuccess = { isLoading = false },
                    onError = { isLoading = false },
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
                if (isLoading) LoadingPlaceholder(modifier = Modifier.fillMaxSize())
            }
    }
}

@Composable
private fun LoadingPlaceholder(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "rememberTransition")
    val alpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(750),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )
    Box(
        modifier = modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(25))
            .background(CustomTheme.colors.readOnlyFieldColor.copy(alpha = alpha))
    )
}

@Composable
private fun FoodName(food: Food) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = food.name,
        style = MaterialTheme.typography.foodName,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun FoodNutrients(food: Food) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.nutrients_per_100_grams),
            style = MaterialTheme.typography.foodNutrientInformation,
        )
        Spacer(modifier = Modifier.height(8.dp))
        NutrientsPercentageLine(food = food)
    }
}

@Composable
private fun NutrientsPercentageLine(
    food: Food
) {
    val totalNutrients = (food.protein + food.fat + food.carbs) + 0.001f
    val proteinPercentage = (food.protein / totalNutrients).toFloat() + 0.001f
    val fatPercentage = (food.fat / totalNutrients).toFloat() + 0.001f
    val carbsPercentage = (food.carbs / totalNutrients).toFloat() + 0.001f
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(25))
                .fillMaxWidth()
        ) {
            NutrientLinePart(
                nutrientName = stringResource(id = R.string.protein_short),
                modifier = Modifier
                    .weight(proteinPercentage),
                color = CustomTheme.colors.proteinColor
            )
            NutrientLinePart(
                nutrientName = stringResource(id = R.string.fat_short),
                modifier = Modifier
                    .weight(fatPercentage),
                color = CustomTheme.colors.fatColor
            )
            NutrientLinePart(
                nutrientName = stringResource(id = R.string.carbs_short),
                modifier = Modifier
                    .weight(carbsPercentage),
                color = CustomTheme.colors.carbsColor
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (food.protein >= 1f) NutrientCount(
                text = food.protein.toString(),
                modifier = Modifier.weight(proteinPercentage)
            ) else Box(modifier = Modifier.weight(proteinPercentage))
            if (food.fat >= 1f) NutrientCount(
                text = food.fat.toString(),
                modifier = Modifier.weight(fatPercentage)
            ) else Box(modifier = Modifier.weight(fatPercentage))
            if (food.carbs >= 1f) NutrientCount(
                text = food.carbs.toString(),
                modifier = Modifier.weight(carbsPercentage)
            ) else Box(modifier = Modifier.weight(carbsPercentage))
        }
    }
}

@Composable
private fun NutrientCount(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.foodNutrientCount,
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
private fun NutrientLinePart(
    nutrientName: String,
    color: Color,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .height(10.dp)
            .fillMaxWidth()
            .background(color)
    ) {
        Text(
            text = nutrientName,
            style = MaterialTheme.typography.foodNutrientShort,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}