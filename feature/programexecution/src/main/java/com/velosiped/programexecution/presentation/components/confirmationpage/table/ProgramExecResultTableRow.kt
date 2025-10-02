package com.velosiped.programexecution.presentation.components.confirmationpage.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programexecution.R
import com.velosiped.programexecution.presentation.components.confirmationpage.table.utils.ProgramExecResultsFields
import com.velosiped.training.traininghistory.repository.TrainingHistory
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.ui.R as coreR

@Composable
fun ProgramExecResultTableRow(
    trainingHistoryItem: TrainingHistory,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(dimensionResource(R.dimen.program_exec_results_row_height))
    ) {
        ProgramExecResultsFields.entries.forEach {
            val value = when (it) {
                ProgramExecResultsFields.EXERCISE -> trainingHistoryItem.exercise
                ProgramExecResultsFields.WEIGHT -> trainingHistoryItem.weight
                ProgramExecResultsFields.REPS_PLANNED -> trainingHistoryItem.repsPlanned
                ProgramExecResultsFields.REPS_DONE -> trainingHistoryItem.reps
            }
            Text(
                text = value.toString(),
                style = CustomTheme.typography.screenMessageSmall,
                maxLines = Int.ONE,
                overflow = TextOverflow.Ellipsis,
                color = CustomTheme.colors.primaryTextColor,
                modifier = Modifier.weight(it.rowWeight)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ProgramExecResultTableRow(
            trainingHistoryItem = TrainingHistory(
                day = 1,
                month = 1,
                year = 1,
                exercise = "Bench press",
                reps = 10,
                repsPlanned = 8,
                weight = 75f
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}