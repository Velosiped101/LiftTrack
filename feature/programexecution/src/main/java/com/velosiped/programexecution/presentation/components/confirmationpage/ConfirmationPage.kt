package com.velosiped.programexecution.presentation.components.confirmationpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programexecution.presentation.components.confirmationpage.table.ProgramExecResultTableHeader
import com.velosiped.programexecution.R
import com.velosiped.programexecution.presentation.components.confirmationpage.table.ProgramExecResultTableRow
import com.velosiped.training.traininghistory.repository.TrainingHistory
import com.velosiped.ui.components.CustomOutlinedButton
import com.velosiped.ui.components.divider.CustomHorizontalDivider
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.ui.R as coreR

@Composable
fun ConfirmationPage(
    trainingHistory: List<TrainingHistory>,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_8)),
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.program_exec_table_header),
            style = CustomTheme.typography.screenMessageMedium,
            color = CustomTheme.colors.primaryTextColor
        )
        ProgramExecResultTableHeader(
            modifier = Modifier.fillMaxWidth()
        )
        CustomHorizontalDivider()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(Float.ONE)
        ) {
            items(trainingHistory) {
                ProgramExecResultTableRow(
                    trainingHistoryItem = it,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        CustomOutlinedButton(
            onClick = onConfirm,
            text = stringResource(id = R.string.program_exec_confirm)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val trainingHistory = listOf(
        TrainingHistory(day = 1, month = 1, year = 1, exercise = "Squat", reps = 4, repsPlanned = 5, weight = 100f),
        TrainingHistory(day = 1, month = 1, year = 1, exercise = "Dead lift", reps = 3, repsPlanned = 5, weight = 120f),
        TrainingHistory(day = 1, month = 1, year = 1, exercise = "Pull ups", reps = 8, repsPlanned = 8, weight = 25f)
    )
    ConfirmationPage(
        trainingHistory = trainingHistory,
        onConfirm = {  }
    )
}