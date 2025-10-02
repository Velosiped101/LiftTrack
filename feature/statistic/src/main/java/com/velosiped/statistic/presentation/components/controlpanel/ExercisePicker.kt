package com.velosiped.statistic.presentation.components.controlpanel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.TWO
import com.velosiped.ui.R as coreR

@Composable
fun ExercisePicker(
    exerciseList: List<String>,
    onExerciseClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius)))
            .background(CustomTheme.colors.boxCardColors.containerColor)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_8)),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(coreR.dimen.space_by_8))
        ) {
            items(exerciseList) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onExerciseClick(it) }
                ) {
                    Text(
                        text = it,
                        style = CustomTheme.typography.screenMessageSmall.copy(textAlign = TextAlign.Start),
                        maxLines = Int.TWO,
                        overflow = TextOverflow.Ellipsis,
                        color = CustomTheme.colors.primaryTextColor
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        ExercisePicker(
            exerciseList = listOf(
                "Bench press",
                "Conventional dead lift with belt on",
                "Egyptian raises"
            ),
            onExerciseClick = {},
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )
    }
}