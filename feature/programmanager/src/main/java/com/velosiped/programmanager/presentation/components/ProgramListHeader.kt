package com.velosiped.programmanager.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programmanager.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.ONE_THIRD
import com.velosiped.ui.R as coreR

@Composable
fun ProgramListHeader(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.program_manager_list_exercise_headline),
            style = CustomTheme.typography.screenMessageSmall,
            color = CustomTheme.colors.primaryTextColor,
            modifier = Modifier.weight(Float.ONE)
        )
        Text(
            text = stringResource(id = R.string.program_manager_list_reps_headline),
            style = CustomTheme.typography.screenMessageSmall,
            color = CustomTheme.colors.primaryTextColor,
            modifier = Modifier.weight(Float.ONE_THIRD)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ProgramListHeader(
            modifier = Modifier.fillMaxWidth()
        )
    }
}