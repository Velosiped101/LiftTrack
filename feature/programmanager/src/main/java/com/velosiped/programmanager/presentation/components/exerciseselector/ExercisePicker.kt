package com.velosiped.programmanager.presentation.components.exerciseselector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programmanager.presentation.utils.ExerciseType
import com.velosiped.training.exercise.repository.Exercise
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.ui.R as coreR
import com.velosiped.utility.extensions.ZERO

@Composable
fun ExercisePicker(
    exerciseList: List<Exercise>,
    onExerciseClick: (Exercise) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyColumnState = rememberLazyListState()
    LaunchedEffect(key1 = exerciseList) {
        lazyColumnState.animateScrollToItem(Int.ZERO)
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius)))
            .background(CustomTheme.colors.boxCardColors.containerColor)
    ) {
        LazyColumn(
            state = lazyColumnState,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
            modifier = Modifier
                .padding(dimensionResource(coreR.dimen.space_by_4))
                .fillMaxHeight()
        ) {
            items(exerciseList) { exercise ->
                Text(
                    text = exercise.name,
                    style = CustomTheme.typography.screenMessageMedium.copy(textAlign = TextAlign.Start),
                    maxLines = Int.ONE,
                    overflow = TextOverflow.Ellipsis,
                    color = CustomTheme.colors.primaryTextColor,
                    modifier = Modifier.clickable { onExerciseClick(exercise) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        ExercisePicker(
            exerciseList = listOf(
                Exercise("Squat", ExerciseType.LEGS.name),
                Exercise("Dead lift", ExerciseType.BACK.name)
            ),
            onExerciseClick = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}