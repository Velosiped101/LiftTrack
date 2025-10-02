package com.velosiped.programmanager.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.programmanager.R
import com.velosiped.programmanager.presentation.utils.ProgramItemState
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.components.CustomSlider
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.SPACE
import com.velosiped.ui.R as coreR

@Composable
fun ProgramExerciseEditor(
    programItemState: ProgramItemState,
    onSetsChange: (Float) -> Unit,
    onRepsChange: (Float) -> Unit,
    onDelete: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val setsMin = integerResource(R.integer.program_manager_min_sets).toFloat()
    val setsMax = integerResource(R.integer.program_manager_max_sets).toFloat()
    val repsMin = integerResource(R.integer.program_manager_min_reps).toFloat()
    val repsMax = integerResource(R.integer.program_manager_max_reps).toFloat()
    val creatingNewProgramItem = programItemState.id == null
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = programItemState.exercise,
            style = CustomTheme.typography.screenMessageMedium,
            color = CustomTheme.colors.primaryTextColor
        )
        if (creatingNewProgramItem) {
            CustomSlider(
                value = programItemState.sets.toFloat(),
                range = setsMin..setsMax,
                onValueChange = { onSetsChange(it) },
                subText = buildString {
                    append(stringResource(id = R.string.program_manager_sets))
                    append(String.SPACE)
                    append(programItemState.sets)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        CustomSlider(
            value = programItemState.reps.toFloat(),
            range = repsMin..repsMax,
            onValueChange = { onRepsChange(it) },
            subText = buildString {
                append(stringResource(id = R.string.program_manager_reps))
                append(String.SPACE)
                append(programItemState.reps)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (!creatingNewProgramItem) {
                CustomIcon(
                    painter = painterResource(id = coreR.drawable.delete),
                    onClick = onDelete
                )
            }
            CustomIcon(
                painter = painterResource(id = coreR.drawable.confirm),
                onClick = onConfirm
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ProgramExerciseEditor(
            programItemState = ProgramItemState(exercise = "Dead lift"),
            onSetsChange = {  },
            onRepsChange = {  },
            onDelete = {  },
            onConfirm = {  },
            modifier = Modifier.fillMaxWidth().height(300.dp)
        )
    }
}