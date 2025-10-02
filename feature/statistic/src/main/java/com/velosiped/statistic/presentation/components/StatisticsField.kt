package com.velosiped.statistic.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.velosiped.statistic.R
import com.velosiped.statistic.presentation.components.table.Table
import com.velosiped.statistic.presentation.model.StatsData
import com.velosiped.statistic.presentation.model.TableData
import com.velosiped.statistic.presentation.model.TableDataValue
import com.velosiped.statistic.presentation.utils.GraphDataFormula
import com.velosiped.ui.components.Graph
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.model.graph.GraphData
import com.velosiped.ui.model.graph.GraphDataValue
import com.velosiped.utility.extensions.ONE
import com.velosiped.ui.R as coreR

@Composable
fun StatisticsField(
    formula: GraphDataFormula,
    data: StatsData,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius)))
            .background(CustomTheme.colors.boxCardColors.containerColor)
    ) {
        when (formula) {
            GraphDataFormula.VOLUME, GraphDataFormula.ONE_REP_MAX -> {
                if (data.graphData != null && data.graphData.values.size > Int.ONE) {
                    Graph(
                        data = data.graphData,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(coreR.dimen.space_by_4))
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.stats_empty_graph_data),
                        style = CustomTheme.typography.screenMessageMedium,
                        color = CustomTheme.colors.primaryTextColor
                    )
                }
            }
            GraphDataFormula.RAW -> {
                if (data.tableData != null) {
                    Table(
                        data = data.tableData,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(coreR.dimen.space_by_4))
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.stats_empty_graph_data),
                        style = CustomTheme.typography.screenMessageMedium,
                        color = CustomTheme.colors.primaryTextColor
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun GraphPreview() {
    val data = GraphData(
        exercise = "Squat",
        values = listOf(
            GraphDataValue(date = "1.1.25", calculatedValue = 120.0),
            GraphDataValue(date = "10.1.25", calculatedValue = 130.0),
            GraphDataValue(date = "20.1.25", calculatedValue = 135.0)
        )
    )
    CustomTheme {
        StatisticsField(
            formula = GraphDataFormula.ONE_REP_MAX,
            data = StatsData(graphData = data),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun TablePreview() {
    val data = TableData(
        exercise = "Dead lift",
        values = listOf(
            TableDataValue(date = "1.1.25", repsPlanned = 6, repsDone = 6, weight = 120f),
            TableDataValue(date = "1.1.25", repsPlanned = 6, repsDone = 5, weight = 120f),
            TableDataValue(date = "1.1.25", repsPlanned = 6, repsDone = 4, weight = 120f),
            TableDataValue(date = "3.1.25", repsPlanned = 5, repsDone = 5, weight = 125f),
            TableDataValue(date = "3.1.25", repsPlanned = 5, repsDone = 5, weight = 125f),
            TableDataValue(date = "3.1.25", repsPlanned = 5, repsDone = 3, weight = 130f),
        )
    )
    CustomTheme {
        StatisticsField(
            formula = GraphDataFormula.RAW,
            data = StatsData(tableData = data),
            modifier = Modifier.fillMaxSize()
        )
    }
}