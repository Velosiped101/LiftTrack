package com.velosiped.notes.presentation.screens.main.components.training

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.SPACE
import com.velosiped.notes.utils.THOUSAND
import com.velosiped.notes.utils.TWO
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.extensions.format
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun Graph(
    exercise: String,
    values: List<Double>,
    dates: List<String>,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        val lineColor = CustomTheme.colors.graphColors.lineColor
        val yAxeDimension = stringResource(id = R.string.graph_indicator_volume)
        val label = exercise + String.SPACE + stringResource(id = R.string.graph_headline_volume)

        val data = listOf(
            Line(
                label = label,
                values = values,
                color = SolidColor(lineColor),
                dotProperties = DotProperties(
                    enabled = true,
                    color = SolidColor(lineColor)
                )
            )
        )
        val labelProperties = LabelProperties(
            enabled = true,
            labels = dates,
            padding = dimensionResource(R.dimen.space_by_4),
            textStyle = CustomTheme.typography.underlineHint,
        )
        val labelHelperProperties = LabelHelperProperties(
            textStyle = CustomTheme.typography.screenMessageSmall
        )
        val indicatorProperties = HorizontalIndicatorProperties(
            textStyle = CustomTheme.typography.underlineHint,
            padding = dimensionResource(R.dimen.space_by_4),
            contentBuilder = { value ->
                (value / Float.THOUSAND).format(Int.TWO) + yAxeDimension
            }
        )

        LineChart(
            data = remember { data },
            curvedEdges = false,
            labelProperties = labelProperties,
            labelHelperProperties = labelHelperProperties,
            indicatorProperties = indicatorProperties
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val exercise = "Bench press"
    val values = listOf(60.0, 65.0, 50.5, 75.0, 90.7, 83.0)
    val dates = listOf("12.03.2025", "14.03.2025", "16.03.2025", "21.03.2025", "25.03.2025", "01.04.2025")
    CustomTheme {
        Graph(
            exercise = exercise,
            values = values,
            dates = dates,
            modifier = Modifier
                .width(400.dp)
                .height(200.dp)
        )
    }
}