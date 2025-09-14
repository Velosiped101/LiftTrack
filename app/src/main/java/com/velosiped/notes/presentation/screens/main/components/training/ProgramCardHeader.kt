package com.velosiped.notes.presentation.screens.main.components.training

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.IconButton
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
import com.velosiped.notes.presentation.screens.components.CustomIcon
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.TrainingState
import com.velosiped.notes.utils.HALF

@Composable
fun ProgramCardHeader(
    currentTrainingState: TrainingState,
    onIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(dimensionResource(R.dimen.program_card_header_height))
    ) {
        Text(
            text = stringResource(currentTrainingState.textId),
            style = CustomTheme.typography.screenMessageMedium
        )
        if (currentTrainingState == TrainingState.TRAINING) {
            IconButton(
                onClick = onIconClicked
            ) {
                CustomIcon(
                    painter = painterResource(id = R.drawable.next),
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
            currentTrainingState = TrainingState.TRAINING,
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
            currentTrainingState = TrainingState.REST,
            onIconClicked = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}