package com.velosiped.statistic.presentation.components.table

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.statistic.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.EMPTY
import com.velosiped.utility.extensions.ONE
import com.velosiped.ui.R as coreR

@Composable
fun TableItem(
    repsPlanned: Int,
    repsDone: Int,
    weightDone: Float,
    modifier: Modifier = Modifier
) {
    val color = if (repsDone >= repsPlanned) CustomTheme.colors.progressColors.achievedColor
    else CustomTheme.colors.progressColors.notAchievedColor
    Box(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = String.EMPTY, modifier = Modifier.weight(Float.ONE))
            Text(
                text = repsDone.toString(),
                style = CustomTheme.typography.screenMessageMedium,
                color = CustomTheme.colors.primaryTextColor,
                modifier = Modifier.weight(Float.ONE)
            )
            Text(
                text = repsPlanned.toString(),
                style = CustomTheme.typography.screenMessageMedium,
                color = CustomTheme.colors.primaryTextColor,
                modifier = Modifier.weight(Float.ONE)
            )
            Text(
                text = weightDone.toString(),
                style = CustomTheme.typography.screenMessageMedium,
                color = CustomTheme.colors.primaryTextColor,
                modifier = Modifier.weight(Float.ONE)
            )
        }
        Canvas(
            modifier = Modifier
                .size(dimensionResource(R.dimen.stats_table_success_indicator_size))
                .align(Alignment.CenterEnd)
        ) {
            drawCircle(color = color)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        TableItem(
            repsPlanned = 10,
            repsDone = 11,
            weightDone = 70f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}