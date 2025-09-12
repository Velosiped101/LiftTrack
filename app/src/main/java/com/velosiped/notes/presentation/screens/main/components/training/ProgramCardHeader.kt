package com.velosiped.notes.presentation.screens.main.components.training

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.ui.theme.screenMessageMedium
import com.velosiped.notes.utils.DayProgress
import com.velosiped.notes.utils.HALF

@Composable
fun ProgramCardHeader(
    currentProgress: DayProgress,
    onIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val day = when (currentProgress) {
        DayProgress.Rest -> stringResource(id = R.string.rest_day)
        DayProgress.Training -> stringResource(id = R.string.training_awaits)
        DayProgress.TrainingFinished -> stringResource(id = R.string.training_done)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(dimensionResource(R.dimen.program_card_header_height))
    ) {
        Text(text = day, style = MaterialTheme.typography.screenMessageMedium)
        if (currentProgress == DayProgress.Training) {
            IconButton(
                onClick = onIconClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier.scale(Float.HALF)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrainingDayPreview() {
    CustomTheme {
        ProgramCardHeader(
            currentProgress = DayProgress.Training,
            onIconClicked = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RestDayPreview() {
    CustomTheme {
        ProgramCardHeader(
            currentProgress = DayProgress.Rest,
            onIconClicked = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}