package com.example.notes.presentation.screens.training.programEditScreen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.R
import com.example.notes.data.local.program.Exercise
import com.example.notes.data.local.program.Program
import com.example.notes.ui.theme.ProgramDay
import com.example.notes.utils.DayOfWeek
import com.example.notes.utils.EMPTY_STRING
import com.example.notes.utils.ExerciseType

@Composable
fun ProgramEditScreen(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    BackHandler {
        onNavigateBack()
    }
    Scaffold(topBar = {
        TopBar(onClick = {
            onNavigateBack()
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            DayPicker(
                uiState = uiState,
                uiAction = uiAction
            )
            ProgramFragment(
                uiState = uiState,
                uiAction = uiAction
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "Customize your program") },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onClick() }
            )
        }
    )
}

@Composable
private fun DayPicker(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit
) {
    val days = DayOfWeek.entries
    val gradient = Brush.verticalGradient(
        colors = listOf(ProgramDay, Color.Transparent),
        startY = Float.POSITIVE_INFINITY,
        endY = 100.0f
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            days.forEach { day ->
                val modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clickable {
                        uiAction(ProgramEditUiAction.SelectProgramDay(day))
                    }
                DayText(
                    text = day.name.slice(0..2),
                    modifier = if (day == uiState.selectedProgramDay) {
                        modifier.drawWithCache {
                            onDrawBehind {
                                drawRect(
                                    brush = gradient
                                )
                            }
                        }
                    } else {
                        modifier
                    }
                )
                if (day != days.last()) VerticalDivider()
            }
        }
    }
}

@Composable
private fun DayText(
    text: String,
    modifier: Modifier,
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(),
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun ProgramFragment(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit
) {
    if (uiState.programForSelectedDay.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ProgramRow(
                exercise = "Exercise",
                reps = "Reps",
                onClick = { }
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(uiState.programForSelectedDay) {
                    ProgramRow(
                        exercise = it.exercise,
                        reps = it.reps.toString(),
                        onClick = {
                            uiAction(ProgramEditUiAction.SelectProgramExercise(it))
                        }
                    )
                }
            }
            IconButton(
                onClick = {
                    uiAction(ProgramEditUiAction.SelectProgramExercise(null))
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_plus),
                    contentDescription = null
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Rest day")
            Text(
                text = "Add exercise",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clickable {
                        uiAction(ProgramEditUiAction.SelectProgramExercise(null))
                    }
            )
        }
    }
    if (uiState.isDialogActive) ExercisePickDialog(
        uiState = uiState,
        uiAction = uiAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExercisePickDialog(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit,
) {
    BackHandler(
        enabled = uiState.selectedProgramExercise != null
    ) {
        uiAction(ProgramEditUiAction.DismissDialog)
    }
    BasicAlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.6f),
        onDismissRequest = { uiAction(ProgramEditUiAction.DismissDialog) }
    ) {
        Card(
            border = BorderStroke(1.dp, SolidColor(Color.Black))
        ) {
            if (uiState.isInSetter) {
                SetsAndRepsSetter(
                    uiState = uiState,
                    uiAction = uiAction
                )
            } else {
                NewExercisePicker(uiState = uiState, uiAction = uiAction)
            }
        }
    }
}

@Composable
private fun NewExercisePicker(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Pick an exercise")
            DropdownExerciseTypeMenu(uiState = uiState, uiAction = uiAction)
        }
        HorizontalDivider()
        LazyColumn {
            items(uiState.exercisesForSelectedType) {
                Text(text = it.name,
                    Modifier.clickable {
                        uiAction(ProgramEditUiAction.SelectNewExercise(it))
                    }
                )
            }
        }
    }
}

@Composable
private fun DropdownExerciseTypeMenu(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Column {
        Text(
            text = uiState.selectedExerciseType.name,
            modifier = Modifier.clickable { isExpanded = true }
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            ExerciseType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(text = type.name) },
                    onClick = {
                        uiAction(ProgramEditUiAction.SelectExerciseType(type))
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SetsAndRepsSetter(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = uiState.selectedProgramExercise?.exercise ?: uiState.selectedNewExercise?.name ?: "why ?", fontSize = 22.sp)
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.selectedProgramExercise == null) {
                    Text(text = "Sets - ${uiState.sets.toInt()}")
                    SetterSlider(
                        initialValue = uiState.sets,
                        range = 1f..5f,
                        onValueChange = { uiAction(ProgramEditUiAction.ChangeSets(it)) }
                    )
                }
                Text(text = "Reps per set - ${uiState.selectedProgramExercise?.reps ?: uiState.reps.toInt()}")
                SetterSlider(
                    initialValue = uiState.selectedProgramExercise?.reps?.toFloat() ?: uiState.reps,
                    range = 1f..20f,
                    onValueChange = { uiAction(ProgramEditUiAction.ChangeReps(it)) }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { uiAction(ProgramEditUiAction.NavigateBackFromSetter) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = null
                    )
                }
                if (uiState.selectedProgramExercise != null)
                    IconButton(onClick = {
                        uiAction(ProgramEditUiAction.DeleteFromProgram)
                    }) {
                        Icon(painter = painterResource(id = R.drawable.delete), contentDescription = null)
                    }
                IconButton(onClick = { uiAction(ProgramEditUiAction.InsertToProgram) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.confirm),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetterSlider(
    initialValue: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Slider(
        value = initialValue,
        onValueChange = { onValueChange(it) },
        valueRange = range,
        track = {
            SliderDefaults.Track(
                sliderState = it,
                colors = SliderDefaults.colors(
                    inactiveTrackColor = Color.Gray,
                    activeTrackColor = Color.Black,
                ),
                modifier = Modifier.height(2.dp)
            )
        },
        thumb = {
            SliderDefaults.Thumb(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color.Black
                )
            )
        }
    )
}

@Composable
fun ProgramRow(
    exercise: String,
    reps: String,
    onClick: (() -> Unit)
) {
    val style = TextStyle(
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = exercise, Modifier.weight(1f), style = style)
        Text(text = reps, Modifier.weight(.2f), style = style)
    }
}