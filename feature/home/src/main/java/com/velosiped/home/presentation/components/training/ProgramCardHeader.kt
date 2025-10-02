package com.velosiped.home.presentation.components.training

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
import com.velosiped.home.R
import com.velosiped.home.presentation.utils.TrainingState
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF
import com.velosiped.ui.R as coreR

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
            style = CustomTheme.typography.screenMessageMedium,
            color = CustomTheme.colors.primaryTextColor
        )
        if (currentTrainingState == TrainingState.AWAITS) {
            IconButton(onClick = onIconClicked) {
                CustomIcon(
                    painter = painterResource(id = coreR.drawable.next),
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
            currentTrainingState = TrainingState.AWAITS,
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