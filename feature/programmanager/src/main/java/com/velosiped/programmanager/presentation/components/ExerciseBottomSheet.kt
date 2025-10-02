package com.velosiped.programmanager.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programmanager.R
import com.velosiped.programmanager.presentation.components.exerciseselector.ExerciseSelector
import com.velosiped.programmanager.presentation.utils.ProgramItemState
import com.velosiped.training.exercise.repository.Exercise
import com.velosiped.ui.components.divider.CustomHorizontalDivider
import com.velosiped.ui.theme.CustomTheme
import kotlinx.coroutines.launch
import com.velosiped.ui.R as coreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseBottomSheet(
    programItemState: ProgramItemState,
    exerciseList: List<Exercise>,
    onDismiss: () -> Unit,
    onExerciseClick: (Exercise) -> Unit,
    onDeleteFromProgram: () -> Unit,
    onAddToProgram: () -> Unit,
    onSetsChange: (Float) -> Unit,
    onRepsChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val creatingNewProgramItem = programItemState.id == null
    var selectingExercise by rememberSaveable {
        mutableStateOf(creatingNewProgramItem)
    }

    fun handleBack() {
        if (!creatingNewProgramItem) { onDismiss() }

        if (selectingExercise) { onDismiss() }
        else { selectingExercise = true }
    }

    BackHandler {
        handleBack()
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState,
        dragHandle = null,
        containerColor = CustomTheme.colors.popUpWindowBackgroundColor,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(coreR.dimen.space_by_12))
        ) {
            val headerText =
                if (creatingNewProgramItem) stringResource(id = R.string.program_manager_exercise_new)
                else stringResource(id = R.string.program_manager_exercise_update)
            ExerciseBottomSheetHeader(
                text = headerText,
                onNavigateBack = { handleBack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(coreR.dimen.space_by_8))
            )
            CustomHorizontalDivider()
            AnimatedContent(targetState = selectingExercise) { targetState ->
                when (targetState) {
                    true -> {
                        ExerciseSelector(
                            onExerciseClick = {
                                onExerciseClick(it)
                                selectingExercise = false
                            },
                            exerciseList = exerciseList,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    false -> {
                        ProgramExerciseEditor(
                            programItemState = programItemState,
                            onSetsChange = onSetsChange,
                            onRepsChange = onRepsChange,
                            onDelete = {
                                scope.launch {
                                    onDeleteFromProgram()
                                    onDismiss()
                                }
                            },
                            onConfirm = {
                                scope.launch {
                                    onAddToProgram()
                                    if (creatingNewProgramItem) {
                                        selectingExercise = true
                                    } else {
                                        onDismiss()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        ExerciseBottomSheet(
            programItemState = ProgramItemState(exercise = "Squat", reps = 8, id = 1),
            exerciseList = emptyList(),
            onDismiss = {  },
            onExerciseClick = {  },
            onDeleteFromProgram = {  },
            onAddToProgram = {  },
            onSetsChange = {  },
            onRepsChange = {  },
            modifier = Modifier.fillMaxHeight(.66f).fillMaxWidth()
        )
    }
}