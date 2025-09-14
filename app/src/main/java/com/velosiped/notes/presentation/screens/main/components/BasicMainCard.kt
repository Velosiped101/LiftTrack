package com.velosiped.notes.presentation.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.main.components.training.Graph
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun BasicMainCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {  }
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(dimensionResource(R.dimen.main_card_corner_radius))
    Box(
        modifier = modifier
            .clip(shape)
            .background(CustomTheme.colors.mainCardColors.containerColor)
            .clickable(indication = null, interactionSource = interactionSource) {
                onClick()
            }
    ) {
        Box(
            modifier = Modifier.padding(dimensionResource(R.dimen.space_by_8))
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BasicMainCard(
        content = {
            Graph(
                exercise = "Exercise 1",
                values = listOf(5.0, 10.0, 7.0),
                dates = listOf("1", "2", "3"),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        },
        onClick = {  },
        modifier = Modifier.fillMaxWidth().background(Color.DarkGray)
    )
}