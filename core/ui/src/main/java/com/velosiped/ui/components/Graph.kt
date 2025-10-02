package com.velosiped.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.model.graph.GraphData
import com.velosiped.ui.model.graph.GraphDataValue
import com.velosiped.utility.extensions.THOUSAND
import com.velosiped.utility.extensions.TWO
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.extensions.format
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun Graph(
    data: GraphData,
    modifier: Modifier = Modifier
) {
    val volumeSign = stringResource(R.string.graph_indicator_volume)
    val lineColor = CustomTheme.colors.graphLineColor
    LineChart(
        data = listOf(
            Line(
                label = data.exercise,
                values = data.values.map { it.calculatedValue },
                color = SolidColor(lineColor),
                dotProperties = DotProperties(
                    enabled = true,
                    color = SolidColor(lineColor)
                )
            )
        ),
        curvedEdges = false,
        labelProperties = LabelProperties(
            enabled = true,
            labels = data.values.map { it.date },
            padding = dimensionResource(R.dimen.space_by_4),
            textStyle = CustomTheme.typography.underlineHint.copy(
                color = CustomTheme.colors.primaryTextColor
            ),
        ),
        labelHelperProperties = LabelHelperProperties(
            textStyle = CustomTheme.typography.screenMessageSmall.copy(
                color = CustomTheme.colors.primaryTextColor
            )
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            textStyle = CustomTheme.typography.underlineHint.copy(
                color = CustomTheme.colors.primaryTextColor
            ),
            padding = dimensionResource(R.dimen.space_by_4),
            contentBuilder = { value ->
                (value / Float.THOUSAND).format(Int.TWO) + volumeSign
            }
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val graphData = GraphData(
        exercise = "Bench press",
        values = listOf(
            GraphDataValue(date = "10.10.2025", calculatedValue = 150.0),
            GraphDataValue(date = "15.10.2025", calculatedValue = 170.0),
            GraphDataValue(date = "21.10.2025", calculatedValue = 200.0)
        )
    )
    CustomTheme {
        Graph(
            data = graphData,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}