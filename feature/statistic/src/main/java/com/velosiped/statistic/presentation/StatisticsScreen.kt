package com.velosiped.statistic.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.statistic.presentation.components.StatisticsField
import com.velosiped.statistic.presentation.components.controlpanel.ControlPanel
import com.velosiped.statistic.presentation.model.StatsData
import com.velosiped.statistic.presentation.model.TableData
import com.velosiped.statistic.presentation.model.TableDataValue
import com.velosiped.statistic.presentation.utils.GraphDataFormula
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.model.graph.GraphData
import com.velosiped.ui.model.graph.GraphDataValue
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.ONE_THIRD
import com.velosiped.ui.R as coreR

@Composable
fun StatisticsScreen(
    exerciseList: List<String>,
    formula: GraphDataFormula,
    data: StatsData,
    onFormulaChange: (GraphDataFormula) -> Unit,
    onExerciseChange: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomTheme.colors.mainBackgroundColor)
            .padding(dimensionResource(coreR.dimen.space_by_8))
    ) {
        StatisticsField(
            formula = formula,
            data = data,
            modifier = Modifier
                .weight(Float.ONE)
                .fillMaxHeight()
                .padding(dimensionResource(coreR.dimen.space_by_4))
        )
        ControlPanel(
            exerciseList = exerciseList,
            formula = formula,
            onFormulaChange = onFormulaChange,
            onExerciseChange = onExerciseChange,
            onNavigateBack = onNavigateBack,
            modifier = Modifier
                .weight(Float.ONE_THIRD)
                .fillMaxHeight()
                .padding(dimensionResource(coreR.dimen.space_by_4))
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun Preview() {
    CustomTheme {
        var formula by remember { mutableStateOf(GraphDataFormula.VOLUME) }
        val data = StatsData(
            graphData = GraphData(
                exercise = "Squat",
                values = listOf(
                    GraphDataValue(date = "1.1.25", calculatedValue = 120.0),
                    GraphDataValue(date = "10.1.25", calculatedValue = 130.0),
                    GraphDataValue(date = "20.1.25", calculatedValue = 135.0)
                )
            ),
            tableData = TableData(
                exercise = "Squat",
                values = listOf(
                    TableDataValue(date = "1.1.25", repsPlanned = 6, repsDone = 6, weight = 120f),
                    TableDataValue(date = "1.1.25", repsPlanned = 6, repsDone = 5, weight = 120f),
                    TableDataValue(date = "1.1.25", repsPlanned = 6, repsDone = 4, weight = 120f),
                    TableDataValue(date = "3.1.25", repsPlanned = 5, repsDone = 5, weight = 125f),
                    TableDataValue(date = "3.1.25", repsPlanned = 5, repsDone = 5, weight = 125f),
                    TableDataValue(date = "3.1.25", repsPlanned = 5, repsDone = 3, weight = 130f),
                )
            )
        )

        StatisticsScreen(
            exerciseList = listOf("Squat"),
            formula = formula,
            data = data,
            onFormulaChange = { formula = it },
            onExerciseChange = {  }
        ) { }
    }
}