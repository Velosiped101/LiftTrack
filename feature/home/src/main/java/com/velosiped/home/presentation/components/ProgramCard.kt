package com.velosiped.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.home.R
import com.velosiped.home.presentation.components.training.ProgramCardHeader
import com.velosiped.home.presentation.utils.TrainingState
import com.velosiped.ui.components.Graph
import com.velosiped.ui.components.divider.CustomHorizontalDivider
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR
import com.velosiped.ui.model.graph.GraphData
import com.velosiped.utility.extensions.ONE

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
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            ProgramCardHeader(
                currentTrainingState = currentTrainingState,
                onIconClicked = onStartTrainingClicked,
                modifier = Modifier.fillMaxWidth()
            )
            CustomHorizontalDivider()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(graphHeight)
            ) {
                if (currentGraphData.values.size > Int.ONE) {
                    Graph(
                        data = currentGraphData,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = stringResource(R.string.main_screen_training_card_empty_graph),
                        style = CustomTheme.typography.screenMessageMedium,
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
        ProgramCard(
            onClick = { },
            currentTrainingState = TrainingState.AWAITS,
            currentGraphData = GraphData(exercise = "", emptyList()),
            onStartTrainingClicked = {  },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}