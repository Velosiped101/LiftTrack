package com.velosiped.ui.components.image

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE

@Composable
fun FoodImage(
    uri: Uri?,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.aspectRatio(Float.ONE)
    ) {
        if (uri != null) {
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.food_image_placeholder),
                contentDescription = null,
                tint = CustomTheme.colors.iconsTintColor,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    FoodImage(
        uri = null,
        modifier = Modifier
            .clip(RoundedCornerShape(15))
            .background(Color.DarkGray)
    )
}