package com.velosiped.notes.presentation.screens.mainScreen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.velosiped.notes.R
import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.ui.theme.screenMessageLarge
import com.velosiped.notes.ui.theme.screenMessageMedium
import com.velosiped.notes.ui.theme.screenMessageSmall
import com.velosiped.notes.ui.theme.underlineHint
import com.velosiped.notes.utils.DEMO_BANNER_YANDEX
import com.velosiped.notes.utils.DayProgress
import com.velosiped.notes.utils.SPACE
import com.velosiped.notes.utils.interpolateColors
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.instream.media3.YandexAdsLoader
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.extensions.format
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MainScreen(
    uiState: MainScreenUiState,
    uiAction: (MainScreenUiAction) -> Unit,
    changesFound: SharedFlow<Boolean>,
    navigateToNewRecipe: () -> Unit,
    navigateToAddMeal: () -> Unit,
    navigateToFoodDbManager: () -> Unit,
    navigateToProgramManager: () -> Unit,
    navigateToProgramExec: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToStatistics: () -> Unit,
    navigateToFeedback: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.surface,
        animationSpec = tween(500),
        label = ""
    )
    var showConfirmationDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = changesFound) {
        changesFound.collect { found ->
            if (found) showConfirmationDialog = true
            else navigateToProgramExec()
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(bgColor)
            .padding(8.dp)
    ) {
        AdBanner(DEMO_BANNER_YANDEX)
        DietCard(
            uiState = uiState,
            navigateToNewRecipe = navigateToNewRecipe,
            navigateToAddMeal = navigateToAddMeal,
            navigateToFoodDbManager = navigateToFoodDbManager
        )
        ProgramCard(
            uiState = uiState,
            uiAction = uiAction,
            navigateToProgramManager = navigateToProgramManager,
            navigateToStatistics = navigateToStatistics
        )
        AdditionalCard(
            onFeedback = { navigateToFeedback() },
            onSettings = { navigateToSettings() })
    }
    if (showConfirmationDialog) {
        ConfirmationDialog(
            onDismiss = { showConfirmationDialog = false },
            onStartNew = {
                uiAction(MainScreenUiAction.ResetProgramProgress)
                navigateToProgramExec()
            },
            onContinue = {
                navigateToProgramExec()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DietCard(
    uiState: MainScreenUiState,
    navigateToNewRecipe: () -> Unit,
    navigateToAddMeal: () -> Unit,
    navigateToFoodDbManager: () -> Unit
) {
    var isDialogActive by remember {
        mutableStateOf(false)
    }
    var showBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val mealHistory = uiState.mealHistory.groupBy { it.time }
    Card(
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        onClick = { isDialogActive = true },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = stringResource(id = R.string.diet_card_header),
                    style = MaterialTheme.typography.screenMessageMedium,
                    maxLines = 1
                )
                Icon(
                    painter = painterResource(id = R.drawable.eye),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(30.dp)
                        .scale(.5f)
                        .alpha(.5f)
                        .clip(RoundedCornerShape(100))
                        .clickable {
                            scope.launch {
                                showBottomSheet = true
                            }
                        }
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
            ){
                CaloriesCircularProgressIndicator(
                    currentValue = uiState.totalCals,
                    targetValue = uiState.targetCalories,
                    modifier = Modifier
                        .weight(.7f)
                        .aspectRatio(1f)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    val maxNutrient = maxOf(uiState.totalProtein, uiState.totalFat, uiState.totalCarbs) + 0.1
                    NutrientIndicator(
                        name = stringResource(R.string.protein),
                        quantity = uiState.totalProtein.toInt(),
                        color = CustomTheme.colors.proteinColor,
                        k = uiState.totalProtein/maxNutrient
                    )
                    NutrientIndicator(
                        name = stringResource(R.string.fat),
                        quantity = uiState.totalFat.toInt(),
                        color = CustomTheme.colors.fatColor,
                        k = uiState.totalFat/maxNutrient
                    )
                    NutrientIndicator(
                        name = stringResource(R.string.carbs),
                        quantity = uiState.totalCarbs.toInt(),
                        color = CustomTheme.colors.carbsColor,
                        k = uiState.totalCarbs/maxNutrient
                    )
                }
            }
        }
    }
    if (isDialogActive) {
        DietDialog(
            onDismiss = { isDialogActive = false },
            onCreateNewRecipe = navigateToNewRecipe,
            onAddMeal = navigateToAddMeal,
            onManageLocalFoodDb = navigateToFoodDbManager
        )
    }
    if (showBottomSheet) {
        MealHistorySheet(
            mealHistory = mealHistory,
            sheetState = bottomSheetState,
            onDismiss = {
                scope.launch {
                    showBottomSheet = false
                }
            }
        )
    }
}

@Composable
private fun CaloriesCircularProgressIndicator(
    currentValue: Int,
    targetValue: Int,
    modifier: Modifier = Modifier
) {
    val progress = currentValue/targetValue.toFloat()
    val sweepAngle = (progress * 360f).coerceIn(0f, 360f)
    val colorsList = listOf(
        CustomTheme.colors.notAchievedColor,
        CustomTheme.colors.littleAchievedColor,
        CustomTheme.colors.almostAchievedColor,
        CustomTheme.colors.achievedColor
    )
    val gradient = Brush.sweepGradient(
        0f to CustomTheme.colors.notAchievedColor,
        .33f to CustomTheme.colors.littleAchievedColor,
        .66f to CustomTheme.colors.almostAchievedColor,
        1f to CustomTheme.colors.achievedColor
    )
    val capColor = interpolateColors(progress, colorsList)
    val strokeWidth = 14.dp
    val strokeCapWidthDifference = 0.dp
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ){
        Canvas(modifier = Modifier
            .fillMaxSize()
            .padding(strokeWidth / 2 + strokeCapWidthDifference)) {
            drawArc(
                color = capColor.copy(alpha = 0.1f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx())
            )
            rotate(-90f) {
                drawArc(
                    brush = gradient,
                    startAngle = 0f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx())
                )
            }
            drawCircle(
                color = capColor,
                radius = strokeWidth.toPx()/2 + strokeCapWidthDifference.toPx(),
                center = Offset(
                    x = center.x * (1 + sin(sweepAngle * PI.toFloat() / 180f)),
                    y = center.y * (1 - cos(sweepAngle * PI.toFloat() / 180f))
                )
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            Text(
                text = currentValue.toString(),
                style = MaterialTheme.typography.screenMessageLarge,
                maxLines = 1
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline, modifier = Modifier.fillMaxWidth())
            Text(
                text = targetValue.toString(),
                style = MaterialTheme.typography.screenMessageSmall,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun NutrientIndicator(
    name: String,
    quantity: Int,
    k: Double,
    color: Color,
    modifier: Modifier = Modifier
) {
    var width by remember {
        mutableStateOf(0.dp)
    }
    val localDensity = LocalDensity.current
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(text = name, style = MaterialTheme.typography.screenMessageSmall)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .onGloballyPositioned {
                    with(localDensity) { width = (it.size.width * 0.8f * k.toFloat()).toDp() }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(width)
                    .clip(
                        RoundedCornerShape(25)
                    )
                    .background(color)
            )
            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.underlineHint,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MealHistoryItem(
    name: String,
    mass: Int,
    cals: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        MealHistoryRowText(text = name, modifier = Modifier.weight(1f))
        MealHistoryRowText(text = stringResource(id = R.string.mass, mass), modifier = Modifier.weight(.3f))
        MealHistoryRowText(text = stringResource(id = R.string.cals, cals), modifier = Modifier.weight(.3f))
    }
}

@Composable
private fun MealHistoryRowText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.screenMessageSmall.copy(textAlign = TextAlign.Start),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DietDialog(
    onDismiss: () -> Unit,
    onCreateNewRecipe: () -> Unit,
    onAddMeal: () -> Unit,
    onManageLocalFoodDb: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NavigationIconButton(
                    modifier = Modifier.weight(1f),
                    iconId = R.drawable.new_recipe,
                    text = stringResource(id = R.string.new_recipe),
                    onClick = { onCreateNewRecipe() }
                )
                NavigationIconButton(
                    modifier = Modifier.weight(1f),
                    iconId = R.drawable.add_meal,
                    text = stringResource(id = R.string.add_meal),
                    onClick = { onAddMeal() }
                )
                NavigationIconButton(
                    modifier = Modifier.weight(1f),
                    iconId = R.drawable.manage_food_db,
                    text = stringResource(id = R.string.food_manager),
                    onClick = { onManageLocalFoodDb() }
                )
            }
        }
    }
}

@Composable
private fun NavigationIconButton(
    iconId: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.size(36.dp)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.screenMessageSmall
        )
    }
}

@Composable
private fun ProgramCard(
    uiState: MainScreenUiState,
    uiAction: (MainScreenUiAction) -> Unit,
    navigateToProgramManager: () -> Unit,
    navigateToStatistics: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDialogActive by remember {
        mutableStateOf(false)
    }
    Card(
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        onClick = { isDialogActive = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(8.dp)
            ) {
                val day = when (uiState.dayProgress) {
                    DayProgress.Rest -> stringResource(id = R.string.rest_day)
                    DayProgress.Training -> stringResource(id = R.string.training_awaits)
                    DayProgress.TrainingFinished -> stringResource(id = R.string.training_done)
                }
                Text(text = day, style = MaterialTheme.typography.screenMessageMedium)
                AnimatedVisibility(
                    visible = uiState.dayProgress == DayProgress.Training
                ) {
                    IconButton(
                        onClick = { uiAction(MainScreenUiAction.CheckForProgramUpdate) },
                        modifier = Modifier.aspectRatio(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.next),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceTint,
                            modifier = Modifier.scale(.5f)
                        )
                    }
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Graph(
                exercise = uiState.currentExercise,
                values = uiState.currentValues,
                dates = uiState.dates
            )
        }
    }
    if (isDialogActive) {
        ProgramDialog(
            onDismiss = { isDialogActive = false },
            onProgramManager = { navigateToProgramManager() },
            onStatistics = {
                isDialogActive = false
                navigateToStatistics()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgramDialog(
    onDismiss: () -> Unit,
    onProgramManager: () -> Unit,
    onStatistics: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NavigationIconButton(
                    modifier = Modifier.weight(1f),
                    iconId = R.drawable.statistics,
                    text = stringResource(id = R.string.statistics),
                    onClick = { onStatistics() }
                )
                NavigationIconButton(
                    modifier = Modifier.weight(1f),
                    iconId = R.drawable.manage_program,
                    text = stringResource(id = R.string.manage_program),
                    onClick = { onProgramManager() }
                )
            }
        }
    }
}

@Composable
private fun Graph(
    exercise: String?,
    values: List<Double>,
    dates: List<String>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        val graphColor = MaterialTheme.colorScheme.outline
        val indicatorText = stringResource(id = R.string.graph_indicator_volume)
        val headlineText = SPACE + stringResource(id = R.string.graph_headline_volume)
        AnimatedContent(
            targetState = exercise,
            label = "",
            modifier = Modifier.padding(4.dp)
        ) { targetState ->
            when (targetState) {
                null -> Text(
                    text = stringResource(id = R.string.empty_graph_data),
                    style = MaterialTheme.typography.screenMessageSmall
                )

                else -> AnimatedContent(
                    targetState = values.size > 1,
                    label = "size_visibility"
                ) {
                    when (it) {
                        true -> LineChart(
                            data = remember {
                                listOf(
                                    Line(
                                        label = targetState + headlineText,
                                        values = values,
                                        color = SolidColor(graphColor),
                                        dotProperties = DotProperties(
                                            enabled = true,
                                            color = SolidColor(graphColor)
                                        )
                                    )
                                )
                            },
                            labelProperties = LabelProperties(
                                enabled = true,
                                labels = dates,
                                padding = 4.dp,
                                textStyle = MaterialTheme.typography.underlineHint,
                            ),
                            labelHelperProperties = LabelHelperProperties(
                                textStyle = MaterialTheme.typography.screenMessageSmall
                            ),
                            indicatorProperties = HorizontalIndicatorProperties(
                                textStyle = MaterialTheme.typography.underlineHint,
                                padding = 4.dp,
                                contentBuilder = { value ->
                                    (value / 1000.0).format(2) + indicatorText
                                }
                            )
                        )

                        false -> Text(
                            text = stringResource(
                                id = R.string.not_enough_graph_data,
                                targetState
                            ),
                            style = MaterialTheme.typography.screenMessageSmall
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MealHistorySheet(
    mealHistory: Map<String, List<MealHistory>>,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        sheetState.show()
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = null,
        sheetState = sheetState
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .fillMaxHeight(.5f)
                .padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.meal_history), style = MaterialTheme.typography.screenMessageMedium)
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            if (mealHistory.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    mealHistory.forEach {
                        stickyHeader {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                            ){
                                Text(
                                    text = it.key,
                                    style = MaterialTheme.typography.screenMessageMedium,
                                    modifier = Modifier.padding(start = 12.dp, 4.dp)
                                )
                            }
                        }
                        items(it.value) { item ->
                            MealHistoryItem(
                                name = item.name,
                                mass = item.mass,
                                cals = item.totalCal.toInt()
                            )
                        }
                    }
                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(
                        text = stringResource(id = R.string.empty_meal_history),
                        style = MaterialTheme.typography.screenMessageSmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfirmationDialog(
    onDismiss: () -> Unit,
    onStartNew: () -> Unit,
    onContinue: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = stringResource(id = R.string.program_changed_message),
                    style = MaterialTheme.typography.screenMessageMedium,
                    modifier = Modifier.padding(8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NavigationIconButton(
                        modifier = Modifier.weight(1f),
                        iconId = R.drawable.resume_session,
                        text = stringResource(id = R.string.continue_previous),
                        onClick = { onContinue() }
                    )
                    NavigationIconButton(
                        modifier = Modifier.weight(1f),
                        iconId = R.drawable.new_session,
                        text = stringResource(id = R.string.start_new),
                        onClick = { onStartNew() }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdBanner(adUnitId: String?) {
    val bannerHeight = LocalConfiguration.current.screenHeightDp * .15f
    Card(
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .height(bannerHeight.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ){
            Text(text = "=)", style = MaterialTheme.typography.screenMessageSmall)
            AndroidView(factory = {
                BannerAdView(it).apply {
                    setAdUnitId(adUnitId)
                    setAdSize(BannerAdSize.stickySize(it, 300))
                    loadAd(AdRequest.Builder().build())
                }
            })
        }
    }
}

@Composable
private fun AdditionalCard(
    onFeedback: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            NavigationIconButton(
                iconId = R.drawable.feedback,
                text = stringResource(id = R.string.feedback),
                onClick = { onFeedback() },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
            VerticalDivider(color = MaterialTheme.colorScheme.outline)
            NavigationIconButton(
                iconId = R.drawable.settings,
                text = stringResource(id = R.string.settings),
                onClick = { onSettings() },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }
    }
}