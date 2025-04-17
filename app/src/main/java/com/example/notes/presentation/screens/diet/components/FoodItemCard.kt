package com.example.notes.presentation.screens.diet.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.notes.R
import com.example.notes.data.local.food.Food

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
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                } else
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(food.imageUrl)
                            .build(),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null
                    )
            }
            Column(modifier = Modifier.padding(4.dp)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = food.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Nutrients in 100 grams: \n" +
                            "Protein - ${food.protein} g, " +
                            "Fat - ${food.fat} g, " +
                            "Carb - ${food.carbs} g.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}