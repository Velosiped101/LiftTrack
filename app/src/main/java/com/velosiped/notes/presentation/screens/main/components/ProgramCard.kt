package com.velosiped.notes.presentation.screens.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.CustomHorizontalDivider
import com.velosiped.notes.presentation.screens.main.components.training.Graph
import com.velosiped.notes.presentation.screens.main.components.training.ProgramCardHeader
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.TrainingState
import com.velosiped.notes.utils.GraphData

@Composable
fun ProgramCard(
    currentTrainingState: TrainingState,
    currentGraphData: GraphData,
    onStartTrainingClicked: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val graphHeight = dimensionResource(R.dimen.program_card_graph_height)
    BasicMainCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            ProgramCardHeader(
                currentTrainingState = currentTrainingState,
                onIconClicked = onStartTrainingClicked,
                modifier = Modifier.fillMaxWidth()
            )
            CustomHorizontalDivider()
            if (currentGraphData.exercise != null) {
                Graph(
                    exercise = currentGraphData.exercise,
                    values = currentGraphData.values,
                    dates = currentGraphData.dates,
                    modifier = Modifier.height(graphHeight)
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(graphHeight)
                ) {
                    Text(
                        text = stringResource(R.string.empty_graph_data),
                        style = CustomTheme.typography.screenMessageMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ProgramCard(
            onClick = { },
            currentTrainingState = TrainingState.TRAINING,
            currentGraphData = GraphData(),
            onStartTrainingClicked = {  },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}