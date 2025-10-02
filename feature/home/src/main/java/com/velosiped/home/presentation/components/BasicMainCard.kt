package com.velosiped.home.presentation.components

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.ui.R
import com.velosiped.ui.components.Graph
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.model.graph.GraphData
import com.velosiped.ui.model.graph.GraphDataValue

@Composable
fun BasicMainCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {  }
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(dimensionResource(R.dimen.box_card_corner_radius))
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

@Preview
@Composable
private fun Preview() {
    val values = listOf(
        GraphDataValue(date = "15.07.25", calculatedValue = 100.0),
        GraphDataValue(date = "20.07.25", calculatedValue = 105.0),
        GraphDataValue(date = "25.07.25", calculatedValue = 120.0)
    )
    CustomTheme {
        BasicMainCard(
            content = {
                Graph(
                    data = GraphData(
                        exercise = "Dead lift",
                        values = values
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
    }
}