package com.velosiped.programexecution.presentation.components.programpage.weightsetter

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.velosiped.programexecution.presentation.utils.WeightIncrement
import com.velosiped.programexecution.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.MINUS
import com.velosiped.utility.extensions.PLUS
import com.velosiped.utility.extensions.SPACE
import com.velosiped.ui.R as coreR

@Composable
fun WeightSetter(
    weight: Float,
    initialWeight: Float,
    showWeightIncreaseHint: Boolean,
    onIncrease: (Float) -> Unit,
    onDecrease: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius)))
            .background(CustomTheme.colors.boxCardColors.containerColor)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_8)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(coreR.dimen.space_by_8))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                WeightIncrement.entries.reversed().forEach {
                    WeightChangeButton(
                        increment = it,
                        prefix = String.PLUS,
                        onClick = { onIncrease(it.weight) }
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(targetState = weight) { targetState ->
                    Text(
                        text = targetState.toString(),
                        style = CustomTheme.typography.screenMessageMedium,
                        color = CustomTheme.colors.primaryTextColor
                    )
                }
                Text(
                    text = stringResource(id = R.string.program_exec_kg),
                    style = CustomTheme.typography.screenMessageSmall,
                    color = CustomTheme.colors.primaryTextColor
                )
                if (showWeightIncreaseHint) {
                    Text(
                        text = buildString {
                            append(stringResource(id = R.string.program_exec_increase_hint))
                            append(String.SPACE)
                            append(initialWeight)
                        },
                        style = CustomTheme.typography.underlineHint,
                        color = CustomTheme.colors.primaryTextColor
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                WeightIncrement.entries.forEach {
                    WeightChangeButton(
                        increment = it,
                        prefix = String.MINUS,
                        onClick = { onDecrease(it.weight) }
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
        WeightSetter(
            weight = 60f,
            initialWeight = 55f,
            showWeightIncreaseHint = true,
            onIncrease = { },
            onDecrease = { }
        )
    }
}