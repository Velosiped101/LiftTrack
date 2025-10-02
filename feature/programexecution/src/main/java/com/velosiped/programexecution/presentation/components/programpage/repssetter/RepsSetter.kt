package com.velosiped.programexecution.presentation.components.programpage.repssetter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programexecution.R
import com.velosiped.ui.components.CustomSlider
import com.velosiped.ui.components.divider.CustomHorizontalDivider
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.SPACE
import com.velosiped.ui.R as coreR

@Composable
fun RepsSetter(
    exercise: String,
    repsPlanned: Int,
    reps: Int,
    onRepsChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val minReps = integerResource(R.integer.program_exec_reps_min).toFloat()
    val maxReps = (repsPlanned + integerResource(R.integer.program_exec_reps_max_overtop)).toFloat()
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius)))
            .background(CustomTheme.colors.boxCardColors.containerColor)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_12)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(coreR.dimen.space_by_4))
        ) {
            Text(
                text = exercise,
                style = CustomTheme.typography.screenMessageLarge,
                color = CustomTheme.colors.primaryTextColor
            )
            CustomHorizontalDivider()
            Text(
                text = buildString {
                    append(stringResource(id = R.string.program_exec_results_reps_planned))
                    append(String.SPACE)
                    append(repsPlanned.toString())
                },
                style = CustomTheme.typography.screenMessageSmall,
                color = CustomTheme.colors.primaryTextColor
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.program_exec_reps_setter_spacer_height)))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                RepsCounterBackgroundBlur(
                    progress = reps.toFloat() / repsPlanned,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.program_exec_reps_counter_blur_height))
                )
                Text(
                    text = reps.toString(),
                    style = CustomTheme.typography.counterExtraLarge,
                    color = CustomTheme.colors.primaryTextColor
                )
            }
            CustomSlider(
                value = reps.toFloat(),
                onValueChange = { onRepsChange(it) },
                range = minReps..maxReps,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var reps by remember { mutableIntStateOf(1) }
    CustomTheme {
        RepsSetter(
            exercise = "Bench press",
            repsPlanned = 10,
            reps = reps,
            onRepsChange = { reps = it.toInt() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}