package com.velosiped.statistic.presentation.components.controlpanel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.statistic.presentation.utils.GraphDataFormula
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

@Composable
fun ControlPanel(
    exerciseList: List<String>,
    formula: GraphDataFormula,
    onFormulaChange: (GraphDataFormula) -> Unit,
    onExerciseChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        modifier = modifier
    ) {
        ControlPanelHeader(
            onNavigateBack = onNavigateBack,
            modifier = Modifier.fillMaxWidth()
        )
        FormulaPicker(
            currentFormula = formula,
            onFormulaClick = { onFormulaChange(it) },
            modifier = Modifier.fillMaxWidth()
        )
        ExercisePicker(
            exerciseList = exerciseList,
            onExerciseClick = { onExerciseChange(it) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        ControlPanel(
            exerciseList = listOf("Bench press", "Squat", "Dead lift"),
            formula = GraphDataFormula.RAW,
            onFormulaChange = {  },
            onExerciseChange = {  },
            onNavigateBack = {  },
            modifier = Modifier.fillMaxSize()
        )
    }
}