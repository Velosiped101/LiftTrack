package com.velosiped.notes.presentation.screens.training.programEditScreen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.velosiped.notes.R
import com.velosiped.notes.data.database.exercise.Exercise
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.ui.theme.screenMessageLarge
import com.velosiped.notes.ui.theme.screenMessageMedium
import com.velosiped.notes.ui.theme.screenMessageSmall
import com.velosiped.notes.ui.theme.tableHeadline
import com.velosiped.notes.ui.theme.tableItems
import com.velosiped.notes.ui.theme.topBarHeadline
import com.velosiped.notes.utils.DayOfWeek
import com.velosiped.notes.utils.EMPTY_STRING
import com.velosiped.notes.utils.ExerciseType
import com.velosiped.notes.utils.SPACE
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun ProgramEditScreen(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val showBottomSheet = rememberSaveable {
        mutableStateOf(false)
    }
    var showDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val toastText = stringResource(id = R.string.confirm_delete_message)
    fun backHandler() {
        onNavigateBack()
    }
    BackHandler {
        backHandler()
    }
    Scaffold(topBar = {
        TopBar(
            onNavigateBack = { backHandler() },
            onShowDeleteDialog = { showDeleteDialog = true }
        )
    }) { innerPadding ->
        MainPage(
            uiState = uiState,
            uiAction = uiAction,
            showBottomSheet = showBottomSheet,
            modifier = Modifier.padding(innerPadding)
        )
        if (showBottomSheet.value) {
            ExerciseBottomSheet(
                uiState = uiState,
                uiAction = uiAction,
                showBottomSheet = showBottomSheet,
                modifier = Modifier.fillMaxHeight(.5f)
            )
        }
        if (showDeleteDialog) {
            DeleteProgramDialog(
                onDismiss = { showDeleteDialog = false },
                onDropCurrentDayProgram = {
                    uiAction(ProgramEditUiAction.DropProgramForDay)
                    showDeleteDialog = false
                },
                onDropProgram = {
                    uiAction(ProgramEditUiAction.DropProgram)
                    showDeleteDialog = false
                },
                onShowToast = {
                    Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun MainPage(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit,
    showBottomSheet: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val changedFromPicker = rememberSaveable {
        mutableStateOf(false)
    }
    val initialPage = DayOfWeek.entries.indexOf(uiState.day)
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { DayOfWeek.entries.size })
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        DayPicker(
            uiState = uiState,
            uiAction = uiAction,
            changedFromPicker = changedFromPicker,
            pagerState = pagerState
        )
        ProgramFragment(
            uiState = uiState,
            uiAction = uiAction,
            showBottomSheet = showBottomSheet,
            changedFromPicker = changedFromPicker,
            pagerState = pagerState
        )
    }
}

@Composable
private fun DayPicker(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit,
    changedFromPicker: MutableState<Boolean>,
    pagerState: PagerState
) {
    val days = DayOfWeek.entries
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, CustomTheme.colors.selectedOptionColor),
        startY = 0f,
        endY = 150f
    )
    val lightningOffset by animateFloatAsState(
        targetValue = pagerState.currentPage + pagerState.currentPageOffsetFraction,
        label = ""
    )
    var dayTabSize by remember {
        mutableStateOf(Size.Zero)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        Box(modifier = Modifier.fillMaxWidth()){
            Canvas(modifier = Modifier.matchParentSize()) {
                drawRect(
                    brush = gradient,
                    topLeft = Offset(x = lightningOffset * dayTabSize.width, y = 0f),
                    size = dayTabSize
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                days.forEach { day ->
                    DayText(
                        text = day.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                }
                            ) {
                                changedFromPicker.value = true
                                uiAction(ProgramEditUiAction.SelectProgramDay(day))
                            }
                            .onGloballyPositioned { coordinates ->
                                dayTabSize = coordinates.size.let {
                                    Size(it.width.toFloat(), it.height.toFloat())
                                }
                            }
                    )
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun DayText(
    text: String,
    modifier: Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = text.slice(0..2),
            style = MaterialTheme.typography.screenMessageSmall,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ProgramFragment(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit,
    showBottomSheet: MutableState<Boolean>,
    changedFromPicker: MutableState<Boolean>,
    pagerState: PagerState
) {
    LaunchedEffect(key1 = pagerState) {
        if (!changedFromPicker.value) {
            snapshotFlow { pagerState.currentPage }
                .filter { !changedFromPicker.value }
                .collect { page ->
                    val day = DayOfWeek.entries[page]
                    uiAction(ProgramEditUiAction.SelectProgramDay(day))
                }
        }
    }
    LaunchedEffect(key1 = uiState.day) {
        if (changedFromPicker.value) {
            val page = DayOfWeek.entries.indexOf(uiState.day)
            if (page != pagerState.currentPage) pagerState.animateScrollToPage(page)
            changedFromPicker.value = false
        }
    }
    HorizontalPager(state = pagerState, beyondViewportPageCount = 1){ page ->
        ProgramList(uiState = uiState, uiAction = uiAction, page = page, showBottomSheet = showBottomSheet)
    }
}

@Composable
private fun ProgramList(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit,
    showBottomSheet: MutableState<Boolean>,
    page: Int
) {
    val programList = uiState.programList
    if (programList == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val program = programList.filter { it.dayOfWeek == DayOfWeek.entries[page].name }
        AnimatedContent(targetState = program.isNotEmpty(), label = "") { state ->
            when (state) {
                true -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    ProgramRow(
                        exercise = stringResource(id = R.string.program_exercise_headline),
                        reps = stringResource(id = R.string.program_exercise_reps_headline),
                        style = MaterialTheme.typography.tableHeadline
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(program) {
                            ProgramRow(
                                exercise = it.exercise,
                                reps = it.reps.toString(),
                                style = MaterialTheme.typography.tableItems,
                                modifier = Modifier.clickable {
                                    uiAction(ProgramEditUiAction.SelectProgramExercise(it))
                                    showBottomSheet.value = true
                                },
                            )
                        }
                        item {
                            IconButton(
                                onClick = {
                                    uiAction(ProgramEditUiAction.SelectProgramExercise(null))
                                    showBottomSheet.value = true
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_plus),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.surfaceTint,
                                    modifier = Modifier.scale(.4f)
                                )
                            }
                        }
                    }
                }

                false -> Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.program_rest_day),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceTint,
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .aspectRatio(1f)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(id = R.string.empty_program_message),
                            style = MaterialTheme.typography.screenMessageMedium
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(id = R.string.build_program_exercise_message),
                            style = MaterialTheme.typography.screenMessageSmall,
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    }
                                ) {
                                    uiAction(ProgramEditUiAction.SelectProgramExercise(null))
                                    showBottomSheet.value = true
                                }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseBottomSheet(
    uiState: ProgramEditUiState,
    uiAction: (ProgramEditUiAction) -> Unit,
    showBottomSheet: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val creatingNewExercise = uiState.selectedProgramItem?.id == null
    var selectingExercise by rememberSaveable {
        mutableStateOf(creatingNewExercise)
    }
    BackHandler {
        if (selectingExercise) {
            selectingExercise = false
        } else showBottomSheet.value = false
    }
    ModalBottomSheet(
        onDismissRequest = { showBottomSheet.value = false },
        sheetState = bottomSheetState,
        dragHandle = null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val operation = if (creatingNewExercise)
                stringResource(id = R.string.program_edit_goal_new)
            else stringResource(id = R.string.program_edit_goal_update)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = operation,
                style = MaterialTheme.typography.screenMessageLarge
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
            AnimatedContent(targetState = selectingExercise, label = "") { targetState ->
                when (targetState) {
                    true -> ExercisePicker(
                        uiState = uiState,
                        onExercisePicked = {
                            uiAction(ProgramEditUiAction.SelectNewExercise(it))
                            selectingExercise = false
                        },
                        onExerciseTypePicked = { uiAction(ProgramEditUiAction.SelectExerciseType(it)) }
                    )
                    false -> SetsAndRepsSetter(
                        uiState = uiState,
                        onSetsChanged = { uiAction(ProgramEditUiAction.ChangeSets(it)) },
                        onRepsChanged = { uiAction(ProgramEditUiAction.ChangeReps(it)) },
                        onBackButtonClicked = {
                            if (creatingNewExercise) {
                                selectingExercise = true
                            }
                            else scope.launch {
                                bottomSheetState.hide()
                                showBottomSheet.value = false
                            }
                        },
                        onDelete = {
                            scope.launch {
                                uiAction(ProgramEditUiAction.DeleteFromProgram)
                                bottomSheetState.hide()
                                showBottomSheet.value = false
                            }
                        },
                        onConfirm = {
                            scope.launch {
                                uiAction(ProgramEditUiAction.InsertToProgram)
                                if (creatingNewExercise) {
                                    selectingExercise = true
                                }
                                else scope.launch {
                                    bottomSheetState.hide()
                                    showBottomSheet.value = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExercisePicker(
    uiState: ProgramEditUiState,
    onExercisePicked: (Exercise) -> Unit,
    onExerciseTypePicked: (ExerciseType) -> Unit
) {
    fun getIconId(exerciseType: ExerciseType): Int {
        return when (exerciseType) {
            ExerciseType.Chest -> R.drawable.chest_type
            ExerciseType.Back -> R.drawable.back_type
            ExerciseType.Shoulders -> R.drawable.shoulders_type
            ExerciseType.Arms -> R.drawable.arms_type
            ExerciseType.Core -> R.drawable.core_type
            ExerciseType.Legs -> R.drawable.legs_type
        }
    }
    val lazyColumnState = rememberLazyListState()
    LaunchedEffect(key1 = uiState.exerciseType) {
        lazyColumnState.animateScrollToItem(0)
    }
    Row {
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier.weight(1f)
        ) {
            items(uiState.exercisesForSelectedType.sortedBy { it.name }) { exercise ->
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.screenMessageMedium.copy(textAlign = TextAlign.Start),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .clickable {
                            onExercisePicked(exercise)
                        }
                        .padding(4.dp)
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(.2f)
        ) {
            val iconSize = 30.dp
            ExerciseType.entries.forEach {
                val iconTint by animateColorAsState(
                    targetValue = if (it == uiState.exerciseType)
                        CustomTheme.colors.selectedOptionColor
                    else MaterialTheme.colorScheme.surfaceTint,
                    label = ""
                )
                IconButton(onClick = { onExerciseTypePicked(it) }) {
                    Icon(
                        painter = painterResource(id = getIconId(it)),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}

@Composable
private fun SetsAndRepsSetter(
    uiState: ProgramEditUiState,
    onSetsChanged: (Float) -> Unit,
    onRepsChanged: (Float) -> Unit,
    onBackButtonClicked: () -> Unit,
    onDelete: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val exerciseId = uiState.selectedProgramItem?.id
    BackHandler {
        onBackButtonClicked()
    }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = uiState.selectedProgramItem?.exercise ?: EMPTY_STRING,
            style = MaterialTheme.typography.screenMessageMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        if (exerciseId == null) {
            SetterSlider(
                initialValue = uiState.sets.toFloat(),
                range = 1f..5f,
                onValueChange = { onSetsChanged(it) },
                text = stringResource(id = R.string.program_sets) + SPACE + uiState.sets.toString()
            )
        }
        SetterSlider(
            initialValue = uiState.selectedProgramItem?.reps?.toFloat() ?: 1f,
            range = 1f..20f,
            onValueChange = { onRepsChanged(it) },
            text = stringResource(id = R.string.program_reps) + SPACE + uiState.selectedProgramItem?.reps.toString()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onBackButtonClicked() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null
                )
            }
            if (exerciseId != null) {
                IconButton(onClick = { onDelete() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = null
                    )
                }
            }
            IconButton(onClick = { onConfirm() }) {
                Icon(
                    painter = painterResource(id = R.drawable.confirm),
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetterSlider(
    text: String,
    initialValue: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = text,
            style = MaterialTheme.typography.screenMessageMedium
        )
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
}

@Composable
private fun ProgramRow(
    exercise: String,
    reps: String,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
            .clip(RoundedCornerShape(10)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = exercise, Modifier.weight(1f), style = style)
        Text(text = reps, Modifier.weight(.3f), style = style)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onNavigateBack: () -> Unit,
    onShowDeleteDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.program_edit_headline),
                style = MaterialTheme.typography.topBarHeadline,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(.5f)
                )
            }
        },
        actions = {
            IconButton(onClick = { onShowDeleteDialog() }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_menu_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(.5f)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteProgramDialog(
    onDismiss: () -> Unit,
    onDropCurrentDayProgram: () -> Unit,
    onDropProgram: () -> Unit,
    onShowToast: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(onDismissRequest = { onDismiss() }) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DialogIcon(
                            onTap = { onShowToast() },
                            onDoubleTap = { onDropProgram() },
                            painter = painterResource(id = R.drawable.drop),
                            text = stringResource(id = R.string.clear_program),
                            enableDoubleTap = true
                        )
                        DialogIcon(
                            onTap = { onShowToast() },
                            onDoubleTap = { onDropCurrentDayProgram() },
                            painter = painterResource(id = R.drawable.drop_current),
                            text = stringResource(id = R.string.clear_current_day_program),
                            enableDoubleTap = true
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    DialogIcon(
                        onTap = { onDismiss() },
                        onDoubleTap = {  },
                        painter = painterResource(id = R.drawable.back),
                        text = stringResource(id = R.string.return_back),
                        enableDoubleTap = false
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogIcon(
    onTap: () -> Unit,
    onDoubleTap: () -> Unit,
    painter: Painter,
    text: String,
    enableDoubleTap: Boolean
) {
    val modifier = if (enableDoubleTap) Modifier.pointerInput(Unit) {
        detectTapGestures(
            onTap = { onTap() },
            onDoubleTap = { onDoubleTap() }
        )
    } else Modifier.pointerInput(Unit) {
        detectTapGestures(
            onTap = { onTap() }
        )
    }
    Box(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.Center)
        ){
            if (enableDoubleTap) Text(
                text = text,
                style = MaterialTheme.typography.screenMessageSmall
            )
            Icon(
                painter = painter,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
            )
            if (!enableDoubleTap) Text(
                text = text,
                style = MaterialTheme.typography.screenMessageSmall
            )
        }
    }
}