package com.velosiped.programmanager.presentation.components.exerciseselector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programmanager.presentation.utils.ExerciseType
import com.velosiped.training.exercise.repository.Exercise
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.ui.R as coreR

@Composable
fun ExerciseSelector(
    exerciseList: List<Exercise>,
    onExerciseClick: (Exercise) -> Unit,
    modifier: Modifier = Modifier
) {
    var exerciseType by rememberSaveable { mutableStateOf(ExerciseType.BACK) }
    val filteredExerciseList = exerciseList.filter {
        it.type == exerciseType.name
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        modifier = modifier
    ) {
        ExercisePicker(
            exerciseList = filteredExerciseList,
            onExerciseClick = { onExerciseClick(it) },
            modifier = Modifier
                .weight(Float.ONE)
                .fillMaxHeight()
        )
        ExerciseTypePicker(
            currentType = exerciseType,
            onExerciseTypeClick = { exerciseType = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ExerciseSelector(
            exerciseList = listOf(
                Exercise("Squat", ExerciseType.LEGS.name),
                Exercise("Dead lift", ExerciseType.BACK.name)
            ),
            onExerciseClick = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}