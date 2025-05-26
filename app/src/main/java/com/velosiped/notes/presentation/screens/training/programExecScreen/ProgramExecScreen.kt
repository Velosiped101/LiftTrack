package com.velosiped.notes.presentation.screens.training.programExecScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.notes.R
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.ui.theme.largeCounter
import com.velosiped.notes.ui.theme.screenMessageLarge
import com.velosiped.notes.ui.theme.screenMessageMedium
import com.velosiped.notes.ui.theme.screenMessageSmall
import com.velosiped.notes.ui.theme.smallCounter
import com.velosiped.notes.ui.theme.tableHeadline
import com.velosiped.notes.ui.theme.tableItems
import com.velosiped.notes.ui.theme.underlineHint
import com.velosiped.notes.utils.WeightIncrement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun ProgramExecScreen(
    uiState: ProgramExecUiState,
    uiAction: (ProgramExecUiAction) -> Unit,
    onNavigateBack: () -> Unit,
    saveCompleted: Flow<Unit>
) {
    var navigateBackRequested by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        saveCompleted.collect {
            onNavigateBack()
        }
    }
    LaunchedEffect(key1 = navigateBackRequested) {
        if (navigateBackRequested) {
            uiAction(ProgramExecUiAction.UpdateStoredProgress)
            onNavigateBack()
        }
    }
    BackHandler(enabled = !navigateBackRequested) {
        navigateBackRequested = true
    }
    val programProgress = uiState.programProgress
    val showWeightIncreaseHintList = uiState.showHintList
    val initialWeightList = uiState.initialWeight
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopBar {
                if (!navigateBackRequested) navigateBackRequested = true
            }
        }
    ) { innerPadding ->
        if (programProgress != null && showWeightIncreaseHintList != null && initialWeightList != null) {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { uiState.programProgress.size + 1 }
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    HorizontalPager(
                        state = pagerState,
                        userScrollEnabled = false,
                        modifier = Modifier.weight(1f)
                    ) { index ->
                        when (index) {
                            in programProgress.indices -> ProgramPage(
                                programProgressItem = programProgress[index],
                                showWeightIncreaseHint = showWeightIncreaseHintList[index],
                                initialWeight = initialWeightList[index],
                                onRepsChange = { uiAction(ProgramExecUiAction.ChangeRepsDone(reps = it, index = index)) },
                                onWeightChange = { uiAction(ProgramExecUiAction.ChangeWeight(weight = it, index = index)) }
                            )
                            else -> ConfirmationPage(
                                programProgress = programProgress,
                                onConfirm = { uiAction(ProgramExecUiAction.SaveProgress) }
                            )
                        }
                    }
                    LinearProgressIndicator(
                        progress = { pagerState.currentPage.toFloat() / programProgress.size },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                PageNavigationButton(
                    isNextButton = true,
                    isVisible = pagerState.currentPage < pagerState.pageCount - 1,
                    onClick = {
                        scope.launch {
                            uiAction(ProgramExecUiAction.UpdateStoredProgress)
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
                PageNavigationButton(
                    isNextButton = false,
                    isVisible = pagerState.currentPage > 0,
                    onClick = {
                        scope.launch {
                            uiAction(ProgramExecUiAction.UpdateStoredProgress)
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }
        else LoadingScreen(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
private fun ProgramPage(
    programProgressItem: ProgramProgress,
    initialWeight: Float,
    showWeightIncreaseHint: Boolean,
    onRepsChange: (Float) -> Unit,
    onWeightChange: (Float) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = programProgressItem.exercise,
                style = MaterialTheme.typography.screenMessageLarge
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
            )
            Text(
                text = stringResource(id = R.string.planned_reps, programProgressItem.repsPlanned),
                style = MaterialTheme.typography.screenMessageSmall
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ){
            var direction by remember {
                mutableIntStateOf(1)
            }
            RepsTextBox(
                reps = programProgressItem.reps,
                repsPlanned = programProgressItem.repsPlanned,
                direction = direction,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            RepsSlider(
                reps = programProgressItem.reps.toFloat(),
                onValueChange = {
                    direction = if (it > programProgressItem.reps) 1 else -1
                    onRepsChange(it)
                },
                repsPlanned = programProgressItem.repsPlanned.toFloat()
            )
            Text(
                text = stringResource(R.string.done_reps),
                style = MaterialTheme.typography.screenMessageSmall
            )
        }
        Spacer(modifier = Modifier.height(1.dp))
        WeightSetter(
            weight = programProgressItem.weight,
            initialWeight = initialWeight,
            showWeightIncreaseHint = showWeightIncreaseHint,
            onIncrease = {
                val updatedWeight = programProgressItem.weight + it
                if (updatedWeight <= 500f) onWeightChange(programProgressItem.weight + it)
            },
            onDecrease = {
                val updatedWeight = programProgressItem.weight + it
                if (updatedWeight >= 0f) onWeightChange(programProgressItem.weight + it)
            }
        )
    }
}

@Composable
private fun ConfirmationPage(
    programProgress: List<ProgramProgress>,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.progress_execution_check_correctness),
            style = MaterialTheme.typography.screenMessageMedium,
        )
        Spacer(modifier = Modifier.height(24.dp))
        TableItem(
            exercise = stringResource(id = R.string.program_exercise_headline),
            reps = stringResource(id = R.string.program_exercise_reps_headline),
            repsPlanned = stringResource(id = R.string.program_exercise_reps_planned_headline),
            weight = stringResource(id = R.string.program_exercise_weight_headline),
            style = MaterialTheme.typography.tableHeadline,
            enableColorIndication = false
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(programProgress) {
                val color = when {
                    it.reps < it.repsPlanned * 0.75f -> CustomTheme.colors.notAchievedColor
                    it.reps < it.repsPlanned -> CustomTheme.colors.almostAchievedColor
                    else -> CustomTheme.colors.achievedColor
                }
                TableItem(
                    exercise = it.exercise,
                    reps = it.reps.toString(),
                    repsPlanned = it.repsPlanned.toString(),
                    weight = it.weight.toString(),
                    style = MaterialTheme.typography.tableItems,
                    enableColorIndication = true,
                    color = color
                )
            }
        }
        Button(
            onClick = { onConfirm() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Text(text = stringResource(id = R.string.confirm), style = MaterialTheme.typography.screenMessageSmall)
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun TableItem(
    exercise: String,
    reps: String,
    repsPlanned: String,
    weight: String,
    style: TextStyle,
    enableColorIndication: Boolean = true,
    color: Color = Color.Transparent
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
    ) {
        Text(text = exercise, style = style, modifier = Modifier
            .padding(2.dp)
            .weight(1f), overflow = TextOverflow.Ellipsis)
        Text(text = weight, style = style, modifier = Modifier
            .padding(2.dp)
            .weight(.4f), overflow = TextOverflow.Ellipsis)
        Text(text = repsPlanned, style = style, modifier = Modifier
            .padding(2.dp)
            .weight(.25f), overflow = TextOverflow.Ellipsis)
        Box(
            modifier = Modifier
                .padding(2.dp)
                .weight(.25f),
            contentAlignment = Alignment.Center
        ) {
            if (enableColorIndication) {
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                ) {
                    drawRoundRect(
                        color = color,
                        cornerRadius = CornerRadius(10f, 10f),
                        topLeft = Offset(x = size.width * 0.9f, y = 0f)
                    )
                }
            }
            Text(
                text = reps,
                style = style,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PageNavigationButton(
    isNextButton: Boolean,
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier.clickable {
            if (isVisible) onClick()
        }
    ){
        IconButton(onClick = { onClick() }) {
            Icon(
                painter = painterResource(id = R.drawable.next),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.rotate(if (isNextButton) 0f else 180f)
            )
        }
    }
}

@Composable
private fun RepsSlider(
    reps: Float,
    onValueChange: (Float) -> Unit,
    repsPlanned: Float,
) {
    Slider(
        value = reps,
        onValueChange = { onValueChange(it) },
        valueRange = 0f..repsPlanned + 5,
        colors = SliderDefaults.colors(
            activeTrackColor = MaterialTheme.colorScheme.surfaceTint,
            inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            thumbColor = MaterialTheme.colorScheme.outline
        )
    )
}

@Composable
private fun RepsTextBox(
    reps: Int,
    repsPlanned: Int,
    direction: Int,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState( // TODO: try to use lerp
        targetValue = when {
            reps < repsPlanned * 0.75f -> CustomTheme.colors.notAchievedColor
            reps < repsPlanned -> CustomTheme.colors.almostAchievedColor
            else -> CustomTheme.colors.achievedColor
        },
        animationSpec = tween(1000),
        label = "color"
    )
    val colorsList = listOf(color, Color.Transparent)
    val infiniteTransition = rememberInfiniteTransition(label = "transition")
    val backgroundScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ){
        Canvas(modifier = Modifier
            .matchParentSize()
            .blur(radius = 10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            .scale(backgroundScale)
        ) {
            drawRect(
                brush = Brush.horizontalGradient(colors = colorsList),
                topLeft = Offset(x = size.width/2, y = 0f),
                size = Size(width = size.width/2 * 0.9f, height = size.height)
            )
            drawRect(
                brush = Brush.horizontalGradient(colors = colorsList.reversed()),
                topLeft = Offset(x = size.width/2 * 0.1f, y = 0f),
                size = Size(width = size.width/2 * 0.9f, height = size.height)
            )
        }
        AnimatedContent(
            targetState = reps,
            transitionSpec = {
                (slideInHorizontally { it/2 * direction } + fadeIn()).togetherWith(
                    slideOutHorizontally { -it/2 * direction } + fadeOut()
                )
            },
            label = "text"
        ) { targetState ->
            Text(
                text = targetState.toString(),
                style = MaterialTheme.typography.largeCounter,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun WeightSetter(
    weight: Float,
    initialWeight: Float,
    showWeightIncreaseHint: Boolean,
    onIncrease: (Float) -> Unit,
    onDecrease: (Float) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            WeightIncrement.entries.reversed().forEach {
                WeightChangeButton(increment = "+" + it.weight.toString()) {
                    onIncrease(it.weight)
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(targetState = weight, label = ""){ targetState ->
                Text(
                    text = targetState.toString(),
                    style = MaterialTheme.typography.smallCounter
                )
            }
            Text(text = stringResource(id = R.string.kg), style = MaterialTheme.typography.screenMessageSmall)
            if (showWeightIncreaseHint) Text(
                text = stringResource(id = R.string.increase_hint, initialWeight),
                style = MaterialTheme.typography.underlineHint
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            WeightIncrement.entries.forEach {
                WeightChangeButton(increment = "-" + it.weight.toString()) {
                    onDecrease(it.weight * (-1))
                }
            }
        }
    }
}

@Composable
private fun WeightChangeButton(
    increment: String,
    onClick: () -> Unit
) {
    Card(
        onClick = { onClick() },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier.padding(2.dp)
    ) {
        Text(
            text = increment,
            style = MaterialTheme.typography.screenMessageSmall,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {  },
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
        actions = { Box(modifier = Modifier.size(48.dp)) }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ProgramExecPreview() {
    val programProgress = listOf(
        ProgramProgress(
            day = 3,
            month = 3,
            year = 2018,
            reps = 5,
            weight = 115f,
            exercise = "Deadlift",
            repsPlanned = 5
        ),
        ProgramProgress(
            day = 3,
            month = 3,
            year = 2018,
            reps = 2,
            weight = 85f,
            exercise = "Bench press",
            repsPlanned = 5
        ),
        ProgramProgress(
            day = 3,
            month = 3,
            year = 2018,
            reps = 4,
            weight = 102.5f,
            exercise = "Squat",
            repsPlanned = 6
        ),
        ProgramProgress(
            day = 3,
            month = 3,
            year = 2018,
            reps = 9,
            weight = 82.5f,
            exercise = "Vertical row",
            repsPlanned = 8
        ),
    )
//    ProgramExecScreen(
//        uiState = ProgramExecUiState(programProgress = programProgress),
//        uiAction = {},
//        onNavigateBack = { /*TODO*/ },
//        saveCompleted = flowOf()
//    )
    val programProgressItem = ProgramProgress(
        day = 3,
        month = 3,
        year = 2018,
        reps = 9,
        weight = 82.5f,
        exercise = "Vertical row",
        repsPlanned = 8
    )
    TableItem(
        exercise = "dsgsd",
        reps = "10",
        repsPlanned = "15",
        weight = "500",
        style = MaterialTheme.typography.tableItems,
        enableColorIndication = true,
        color = Color.Red
    )
}
