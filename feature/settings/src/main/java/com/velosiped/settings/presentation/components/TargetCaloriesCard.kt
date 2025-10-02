package com.velosiped.settings.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.settings.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

@Composable
fun TargetCaloriesCard(
    targetCalories: Int,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius))
    val borderStroke = BorderStroke(
        width = dimensionResource(coreR.dimen.card_border_width),
        color = CustomTheme.colors.boxCardColors.borderColor
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(CustomTheme.colors.boxCardColors.containerColor)
            .border(borderStroke, shape)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedContent(targetState = targetCalories) { calories ->
                Text(
                    text = calories.toString(),
                    style = CustomTheme.typography.screenMessageLarge,
                    color = CustomTheme.colors.primaryTextColor
                )
            }
            Text(
                text = stringResource(id = R.string.settings_target_calories),
                style = CustomTheme.typography.screenMessageSmall,
                color = CustomTheme.colors.primaryTextColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        TargetCaloriesCard(
            targetCalories = 2570,
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )
    }
}