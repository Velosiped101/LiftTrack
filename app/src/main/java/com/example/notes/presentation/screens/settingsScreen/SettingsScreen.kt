package com.example.notes.presentation.screens.settingsScreen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.ui.theme.CustomTheme
import com.example.notes.ui.theme.screenMessageLarge
import com.example.notes.ui.theme.screenMessageSmall
import com.example.notes.ui.theme.topBarHeadline
import com.example.notes.utils.CalorieSurplus
import com.example.notes.utils.DailyActivityK
import com.example.notes.utils.Sex
import com.example.notes.utils.TextRepresentable
import com.example.notes.utils.ThemeMode
import com.example.notes.utils.getClosestIndex
import com.example.notes.utils.toFloatList

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    uiAction: (SettingsUiAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    val activity = LocalContext.current as? Activity
    fun backHandler() {
        if (uiState.isNotFirstLaunch == true) {
            uiAction(SettingsUiAction.RestoreState)
            onNavigateBack()
        } else {
            activity?.finish()
        }
    }
    val bodyMassList = (30f..250f).toFloatList(0.5f)
    val ageValues = (10..99).toList()
    val heightValues = (100..250).toList()
    val hourValues = (0..23).toList()
    val minuteValues = (0..59).toList()
    val scaffoldColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.background,
        animationSpec = tween(500),
        label = ""
    )
    CustomTheme(darkTheme = when (uiState.appThemeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Dark -> true
        ThemeMode.Light -> false
    }) {
        BackHandler {
            backHandler()
        }
        Scaffold(
            topBar = {
                TopBar {
                    backHandler()
                }
            },
            modifier = Modifier.background(color = scaffoldColor)
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(innerPadding)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp, end = 4.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    SegmentedSelector(
                        onValueSelected = { uiAction(SettingsUiAction.ChangeTheme(it)) },
                        currentValue = uiState.appThemeMode,
                        values = ThemeMode.entries,
                        text = stringResource(R.string.theme)
                    )
                    Text(
                        text = stringResource(id = R.string.reset_time),
                        style = MaterialTheme.typography.screenMessageSmall
                    )
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(25),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 4.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            WheelPicker(
                                onValueSelected = { uiAction(SettingsUiAction.ChangeResetTimeHour(it)) },
                                currentValue = uiState.resetTimeHour,
                                values = hourValues,
                                valueName = stringResource(id = R.string.hour)
                            )
                            VerticalDivider(
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.fillMaxHeight()
                            )
                            WheelPicker(
                                onValueSelected = {
                                    uiAction(
                                        SettingsUiAction.ChangeResetTimeMinute(
                                            it
                                        )
                                    )
                                },
                                currentValue = uiState.resetTimeMinute,
                                values = minuteValues,
                                valueName = stringResource(id = R.string.minute)
                            )
                        }
                    }
                    SegmentedSelector(
                        onValueSelected = { uiAction(SettingsUiAction.ChangeSex(it)) },
                        currentValue = uiState.sex,
                        values = Sex.entries,
                        text = stringResource(R.string.sex)
                    )
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(25),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 4.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            WheelPicker(
                                onValueSelected = { uiAction(SettingsUiAction.ChangeAge(it)) },
                                currentValue = uiState.age,
                                values = ageValues,
                                valueName = stringResource(id = R.string.age)
                            )
                            VerticalDivider(
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.fillMaxHeight()
                            )
                            WheelPicker(
                                onValueSelected = { uiAction(SettingsUiAction.ChangeHeight(it)) },
                                currentValue = uiState.height,
                                values = heightValues,
                                valueName = stringResource(id = R.string.height)
                            )
                            VerticalDivider(
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.fillMaxHeight()
                            )
                            WheelPicker(
                                onValueSelected = { uiAction(SettingsUiAction.ChangeBodyMass(it)) },
                                currentValue = uiState.bodyMass,
                                values = bodyMassList,
                                valueName = stringResource(id = R.string.body_mass)
                            )
                        }
                    }
                    SegmentedSelector(
                        onValueSelected = { uiAction(SettingsUiAction.ChangeDailyActivity(it)) },
                        currentValue = uiState.dailyActivityK,
                        values = DailyActivityK.entries,
                        text = stringResource(R.string.daily_activity),
                    )
                    SegmentedSelector(
                        onValueSelected = { uiAction(SettingsUiAction.ChangeCalorieSurplus(it)) },
                        currentValue = uiState.calorieSurplus,
                        values = CalorieSurplus.entries,
                        text = stringResource(R.string.goal),
                    )
                    TargetCaloriesCard(targetCalories = uiState.autoTargetCalories)
                }
                Button(onClick = {
                    uiAction(SettingsUiAction.ConfirmSettings)
                    onNavigateBack()
                }, modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(
                        text = stringResource(id = R.string.confirm_settings),
                        style = MaterialTheme.typography.screenMessageSmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T: TextRepresentable> SegmentedSelector(
    onValueSelected: (T) -> Unit,
    currentValue: T,
    values: List<T>,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ){
        Text(text = text, style = MaterialTheme.typography.screenMessageSmall)
        Spacer(modifier = Modifier.height(4.dp))
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            values.forEachIndexed { index, value ->
                SegmentedButton(
                    selected = currentValue == value,
                    onClick = { onValueSelected(value) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = values.size
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeBorderColor = MaterialTheme.colorScheme.outline,
                        activeContainerColor = Color.Transparent,
                        inactiveBorderColor = MaterialTheme.colorScheme.outline,
                        inactiveContainerColor = Color.Transparent,
                    )
                ) {
                    Text(
                        text = stringResource(value.textId),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        style = MaterialTheme.typography.screenMessageSmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun <T: Number> WheelPicker(
    onValueSelected: (T) -> Unit,
    currentValue: T,
    values: List<T>,
    valueName: String,
    modifier: Modifier = Modifier
) {
    val height = 40.dp
    val width = 80.dp
    val listState = rememberLazyListState()
    val currentIndex = values.getClosestIndex(currentValue)
    LaunchedEffect(key1 = currentIndex) {
        listState.animateScrollToItem(currentIndex)
    }
    LaunchedEffect(key1 = listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val fixedIndex = listState.firstVisibleItemIndex
            values.getOrNull(fixedIndex)?.let {
                onValueSelected(it)
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .wrapContentHeight()
            .padding(top = 4.dp, bottom = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.two_way_arrow),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier
                    .height(height)
                    .aspectRatio(1f)
            )
            LazyColumn(
                state = listState,
                flingBehavior = rememberSnapFlingBehavior(listState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.size(width, height)
            ) {
                items(values) { value ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(width, height)
                    ){
                        Text(
                            text = value.toString(),
                            style = MaterialTheme.typography.screenMessageLarge
                        )
                    }
                }
            }
        }
        Text(text = valueName, style = MaterialTheme.typography.screenMessageSmall)
    }
}

@Composable
private fun TargetCaloriesCard(
    targetCalories: Int,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(25),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            AnimatedContent(targetState = targetCalories, label = ""){ targetState ->
                Text(
                    text = targetState.toString(),
                    style = MaterialTheme.typography.screenMessageLarge
                )
            }
            Text(
                text = stringResource(id = R.string.target_calories),
                style = MaterialTheme.typography.screenMessageSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = { 
            Text(
                text = stringResource(R.string.settings_headline),
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
        actions = { Box(modifier = Modifier.size(48.dp)) }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    WheelPicker(
        onValueSelected = {},
        currentValue = 12,
        values = listOf(10, 11, 12, 13, 14),
        valueName = "INT"
    )
}