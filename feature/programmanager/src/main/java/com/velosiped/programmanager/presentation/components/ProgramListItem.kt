package com.velosiped.programmanager.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programmanager.presentation.utils.DayOfWeek
import com.velosiped.training.program.repository.Program
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.ONE_THIRD
import com.velosiped.ui.R as coreR

@Composable
fun ProgramListItem(
    programItem: Program,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        modifier = modifier.clickable { onItemClick() }
    ) {
        Text(
            text = programItem.exercise,
            style = CustomTheme.typography.screenMessageSmall,
            color = CustomTheme.colors.primaryTextColor,
            modifier = Modifier.weight(Float.ONE)
        )
        Text(
            text = programItem.reps.toString(),
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
        ProgramListItem(
            programItem = Program(exercise = "Squat", day = DayOfWeek.MONDAY.name, reps = 8),
            onItemClick = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}