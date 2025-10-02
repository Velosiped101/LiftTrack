package com.velosiped.programexecution.presentation.components.confirmationpage.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programexecution.R
import com.velosiped.programexecution.presentation.components.confirmationpage.table.utils.ProgramExecResultsFields
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.TWO
import com.velosiped.ui.R as coreR

@Composable
fun ProgramExecResultTableHeader(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(dimensionResource(R.dimen.program_exec_results_row_height))
    ) {
        ProgramExecResultsFields.entries.forEach {
            Text(
                text = stringResource(it.textResId),
                style = CustomTheme.typography.screenMessageSmall,
                maxLines = Int.TWO,
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
        ProgramExecResultTableHeader()
    }
}