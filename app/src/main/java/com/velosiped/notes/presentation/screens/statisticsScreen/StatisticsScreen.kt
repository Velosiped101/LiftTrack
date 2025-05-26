package com.velosiped.notes.presentation.screens.statisticsScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.notes.R
import com.velosiped.notes.domain.GraphDataFormula
import com.velosiped.notes.domain.ProgramData
import com.velosiped.notes.domain.RawGraphData
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.ui.theme.screenMessageLarge
import com.velosiped.notes.ui.theme.screenMessageMedium
import com.velosiped.notes.ui.theme.screenMessageSmall
import com.velosiped.notes.ui.theme.underlineHint
import com.velosiped.notes.utils.Date
import com.velosiped.notes.utils.EMPTY_STRING
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.extensions.format
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun StatisticsScreen(
    uiState: StatisticsUiState,
    uiAction: (StatisticsUiAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Graph(
            exercise = uiState.exercise,
            formula = uiState.formula,
            graphData = uiState.values,
            dates = uiState.dates,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
        ControlPanel(
            exercises = uiState.exercises,
            currentFormula = uiState.formula,
            onFormulaChanged = { uiAction(StatisticsUiAction.SetFormula(it)) },
            onExerciseChanged = { uiAction(StatisticsUiAction.SetExercise(it)) },
            onNavigateBack = onNavigateBack,
            modifier = Modifier.weight(.3f)
        )
    }
}

@Composable
private fun Graph(
    exercise: String?,
    formula: GraphDataFormula,
    graphData: List<ProgramData>,
    dates: List<String>,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
    ) {
        val indicatorText = stringResource(id = R.string.graph_indicator_volume)
        when (exercise) {
            null -> Text(
                text = stringResource(id = R.string.empty_graph_data),
                style = MaterialTheme.typography.screenMessageMedium
            )
            else -> Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.matchParentSize()
            ){
                Text(text = exercise, style = MaterialTheme.typography.screenMessageLarge)
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                AnimatedContent(targetState = formula, label = "label") { targetFormula ->
                    when (targetFormula) {
                        GraphDataFormula.Volume, GraphDataFormula.OneRepMax -> {
                            val labeledData = getLabeledGraphData(graphData = graphData, formula = targetFormula)
                            Chart(
                                labeledData = labeledData,
                                dates = dates,
                                indicatorText = indicatorText,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        GraphDataFormula.Raw -> {
                            Table(
                                graphData = graphData,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Chart(
    labeledData: List<LabeledGraphData>,
    dates: List<String>,
    indicatorText: String,
    modifier: Modifier = Modifier
) {
    LineChart(
        data = labeledData.map { data ->
            Line(
                label = data.label,
                values = data.values,
                color = SolidColor(data.color),
                dotProperties = DotProperties(
                    enabled = true,
                    color = SolidColor(data.color)
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
        ),
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Table(
    graphData: List<ProgramData>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(key1 = graphData.size) {
        scrollState.animateScrollToItem(graphData.lastIndex)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ){
        TableHeader(modifier = Modifier.fillMaxWidth())
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            graphData.forEach { data ->
                val dataLists = data.rawData?.let {
                    it.repsPlanned.zip(it.reps).zip(it.weight)
                }?.map { Triple(it.first.first, it.first.second, it.second) } ?: emptyList()
                stickyHeader {
                    val date = data.date.let { dateTriple ->
                        val day = dateTriple.first.let { if (it < 10) "0$it" else it.toString() }
                        val monthId = Date.getMonthNameId(dateTriple.second)
                        val month = if (monthId != null) stringResource(id = monthId) else dateTriple.second
                        val year = dateTriple.third.toString()
                        "$day $month $year"
                    }
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.screenMessageMedium,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
                items(dataLists) { values ->
                    val repsPlanned = values.first.toInt()
                    val repsDone = values.second.toInt()
                    val weightDone = values.third
                    val color = if (repsDone >= repsPlanned) CustomTheme.colors.achievedColor else CustomTheme.colors.notAchievedColor
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TableRowText(text = EMPTY_STRING, modifier = Modifier.weight(1f))
                            TableRowText(text = repsPlanned.toString(), modifier = Modifier.weight(1f))
                            TableRowText(text = repsDone.toString(), modifier = Modifier.weight(1f))
                            TableRowText(text = weightDone.toString(), modifier = Modifier.weight(1f))
                        }
                        Canvas(modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.CenterEnd)) {
                            drawCircle(color = color)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TableRowText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.screenMessageMedium,
        modifier = modifier
    )
}

@Composable
private fun TableHeader(modifier: Modifier = Modifier) {
    val headlines = listOf(
        EMPTY_STRING,
        stringResource(id = R.string.stat_table_reps_planned),
        stringResource(id = R.string.stat_table_reps),
        stringResource(id = R.string.stat_table_weight)
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        headlines.forEach {
            Text(
                text = it,
                style = MaterialTheme.typography.screenMessageMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ControlPanel(
    exercises: List<String>,
    currentFormula: GraphDataFormula,
    onFormulaChanged: (GraphDataFormula) -> Unit,
    onExerciseChanged: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.return_back),
                style = MaterialTheme.typography.screenMessageSmall
            )
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier.scale(.75f)
                )
            }
        }
        FormulaPicker(currentFormula = currentFormula, onClick = { onFormulaChanged(it) })
        ExercisePicker(
            exercises = exercises,
            onClick = { onExerciseChanged(it) },
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
    }
}

@Composable
private fun FormulaPicker(
    currentFormula: GraphDataFormula,
    onClick: (GraphDataFormula) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var dropdownMenuWidth by remember {
        mutableStateOf(0)
    }
    val angle by animateFloatAsState(targetValue = if (expanded) 180f else 0f, label = "")
    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .onGloballyPositioned {
                dropdownMenuWidth = it.size.width
            }
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.formula),
                style = MaterialTheme.typography.screenMessageMedium
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { expanded = true }
            ) {
                Text(
                    text = stringResource(currentFormula.textId),
                    style = MaterialTheme.typography.screenMessageSmall
                )
                Icon(
                    painter = painterResource(id = R.drawable.expand_meal_history),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .scale(.5f)
                        .rotate(angle)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .width(with(LocalDensity.current) { dropdownMenuWidth.toDp() })
                    .fillMaxHeight()
            ) {
                GraphDataFormula.entries.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(it.textId),
                                style = MaterialTheme.typography.screenMessageSmall
                            )
                        },
                        onClick = {
                            onClick(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExercisePicker(
    exercises: List<String>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.exercise),
                style = MaterialTheme.typography.screenMessageMedium
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(exercises) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .clickable { onClick(it) }
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.screenMessageSmall,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getLabeledGraphData(
    graphData: List<ProgramData>,
    formula: GraphDataFormula
): List<LabeledGraphData> {
    return when (formula) {
        GraphDataFormula.Volume -> listOf(
            LabeledGraphData(
                values = graphData.map { it.value ?: 0.0 },
                label = stringResource(id = R.string.graph_total_volume),
                color = CustomTheme.colors.volumeColor
            )
        )

        GraphDataFormula.OneRepMax -> listOf(
            LabeledGraphData(
                values = graphData.map { it.value ?: 0.0 },
                label = stringResource(id = R.string.graph_one_rep_max),
                color = CustomTheme.colors.oneRepMaxColor
            )
        )

        else -> listOf()
    }
}

@Composable
private fun getRawTableData(
    graphData: List<ProgramData>
): List<List<RawGraphData>> {
    return listOf(
        graphData.mapNotNull { it.rawData }
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ControlPanel(
        exercises = listOf("Bench press", "Dead lift", "Squat"),
        currentFormula = GraphDataFormula.Volume,
        onFormulaChanged = {},
        onExerciseChanged = {},
        onNavigateBack = { /*TODO*/ })
}