package com.velosiped.programmanager.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programmanager.R
import com.velosiped.programmanager.presentation.components.DayPicker
import com.velosiped.programmanager.presentation.components.DeleteProgramPopUp
import com.velosiped.programmanager.presentation.components.ExerciseBottomSheet
import com.velosiped.programmanager.presentation.components.ProgramList
import com.velosiped.programmanager.presentation.utils.DayOfWeek
import com.velosiped.programmanager.presentation.utils.ProgramItemState
import com.velosiped.training.exercise.repository.Exercise
import com.velosiped.training.program.repository.Program
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.components.ScreenMessage
import com.velosiped.ui.components.topbar.BaseTopBar
import com.velosiped.ui.components.topbar.TopBarHeader
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.TWO_THIRDS
import kotlinx.coroutines.flow.filter
import com.velosiped.ui.R as coreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramEditScreen(
    exerciseList: List<Exercise>,
    programList: List<Program>,
    programItemState: ProgramItemState,
    onDaySelect: (DayOfWeek) -> Unit,
    onProgramItemClick: (Program) -> Unit,
    onExerciseClick: (Exercise) -> Unit,
    onAddNewClick: () -> Unit,
    onDeleteFromProgram: () -> Unit,
    onAddToProgram: () -> Unit,
    onSetsChange: (Float) -> Unit,
    onRepsChange: (Float) -> Unit,
    onDropProgramForTheDay: () -> Unit,
    onDropProgram: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    var changedFromPicker by remember { mutableStateOf(false) }
    var userScrollEnabled by remember { mutableStateOf(true) }

    val initialPage = DayOfWeek.entries.indexOf(programItemState.day)
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { DayOfWeek.entries.size })

    LaunchedEffect(key1 = pagerState) {
        if (!changedFromPicker) {
            snapshotFlow { pagerState.currentPage }
                .filter { !changedFromPicker }
                .collect { page ->
                    val day = DayOfWeek.entries[page]
                    onDaySelect(day)
                }
        }
    }

    LaunchedEffect(key1 = programItemState.day) {
        if (changedFromPicker) {
            userScrollEnabled = false
            val page = DayOfWeek.entries.indexOf(programItemState.day)
            if (page != pagerState.currentPage) pagerState.animateScrollToPage(page)
            changedFromPicker = false
            userScrollEnabled = true
        }
    }

    Scaffold(
        topBar = {
            BaseTopBar(
                onNavigateBack = { onNavigateBack() },
                header = { TopBarHeader(text = stringResource(R.string.program_manager_headline)) },
                action = {
                    CustomIcon(
                        painter = painterResource(id = coreR.drawable.baseline_menu_24),
                        onClick = { showDeleteDialog = true }
                    )
                }
            )
        },
        containerColor = CustomTheme.colors.mainBackgroundColor
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DayPicker(
                day = programItemState.day,
                onDayChange = {
                    changedFromPicker = true
                    onDaySelect(it)
                },
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = Int.ONE,
                userScrollEnabled = userScrollEnabled
            ) { page ->
                val filteredProgram = programList.filter { it.day.uppercase() == DayOfWeek.entries[page].name }

                if (filteredProgram.isNotEmpty()) {
                    ProgramList(
                        programList = filteredProgram,
                        onProgramItemClick = {
                            onProgramItemClick(it)
                            showBottomSheet = true
                        },
                        onAddNewClick = {
                            onAddNewClick()
                            showBottomSheet = true
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(coreR.dimen.space_by_8))
                    )
                } else {
                    ScreenMessage(
                        message = stringResource(R.string.program_manager_empty_program),
                        painter = painterResource(coreR.drawable.program_rest_day),
                        subtext = stringResource(R.string.program_manager_build_program),
                        onSubtextClicked = {
                            onAddNewClick()
                            showBottomSheet = true
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        if (showBottomSheet) {
            ExerciseBottomSheet(
                programItemState = programItemState,
                exerciseList = exerciseList,
                onDismiss = { showBottomSheet = false },
                onExerciseClick = onExerciseClick,
                onDeleteFromProgram = onDeleteFromProgram,
                onAddToProgram = onAddToProgram,
                onSetsChange = onSetsChange,
                onRepsChange = onRepsChange,
                modifier = Modifier.fillMaxHeight(Float.TWO_THIRDS)
            )
        }
        if (showDeleteDialog) {
            BasicAlertDialog(
                onDismissRequest = { showDeleteDialog = false }
            ) {
                DeleteProgramPopUp(
                    onDropCurrentDayProgram = {
                        onDropProgramForTheDay()
                        showDeleteDialog = false
                    },
                    onDropProgram = {
                        onDropProgram()
                        showDeleteDialog = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val exerciseList = emptyList<Exercise>()
    val programList = listOf(Program(exercise = "Squat", day = DayOfWeek.SATURDAY.name, reps = 8))
    var day by remember { mutableStateOf(DayOfWeek.MONDAY) }
    CustomTheme {
        ProgramEditScreen(
            exerciseList = exerciseList,
            programList = programList,
            programItemState = ProgramItemState(day = day),
            onDaySelect = { day = it },
            onProgramItemClick = {  },
            onExerciseClick = {  },
            onAddNewClick = {  },
            onDeleteFromProgram = {  },
            onAddToProgram = {  },
            onSetsChange = {  },
            onRepsChange = {  },
            onDropProgramForTheDay = {  },
            onDropProgram = {  }
        ) { }
    }
}