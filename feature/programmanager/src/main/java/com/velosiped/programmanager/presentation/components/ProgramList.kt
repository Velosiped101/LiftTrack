package com.velosiped.programmanager.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programmanager.presentation.utils.DayOfWeek
import com.velosiped.training.program.repository.Program
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.components.divider.CustomHorizontalDivider
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF
import com.velosiped.ui.R as coreR

@Composable
fun ProgramList(
    programList: List<Program>,
    onProgramItemClick: (Program) -> Unit,
    onAddNewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius)))
            .background(CustomTheme.colors.boxCardColors.containerColor)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(coreR.dimen.space_by_4))
        ) {
            ProgramListHeader()
            CustomHorizontalDivider()
            LazyColumn(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(programList) {
                    ProgramListItem(
                        programItem = it,
                        onItemClick = { onProgramItemClick(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    CustomIcon(
                        painter = painterResource(id = coreR.drawable.add_plus),
                        onClick = { onAddNewClick() },
                        modifier = Modifier.scale(Float.HALF)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ProgramList(
            programList = listOf(
                Program(exercise = "Squat", day = DayOfWeek.MONDAY.name, reps = 8),
                Program(exercise = "Dead lift", day = DayOfWeek.MONDAY.name, reps = 5),
            ),
            onProgramItemClick = {  },
            onAddNewClick = {  },
            modifier = Modifier.fillMaxSize()
        )
    }
}