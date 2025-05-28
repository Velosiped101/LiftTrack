package com.velosiped.notes.presentation.screens.diet.addMealScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.velosiped.notes.R
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.presentation.screens.diet.components.FoodItemCard
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.ui.theme.pickedFood
import com.velosiped.notes.ui.theme.screenMessageLarge
import com.velosiped.notes.ui.theme.screenMessageMedium
import com.velosiped.notes.ui.theme.screenMessageSmall
import com.velosiped.notes.ui.theme.searchBarInput
import com.velosiped.notes.ui.theme.searchCheckbox
import com.velosiped.notes.ui.theme.underlineHint
import com.velosiped.notes.utils.EMPTY_STRING
import com.velosiped.notes.utils.SearchMode
import com.velosiped.notes.utils.interpolateColors
import com.velosiped.notes.utils.toHttpError
import com.velosiped.notes.utils.toIOError
import ir.ehsannarmani.compose_charts.extensions.format
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@Composable
fun AddMealScreen(
    uiState: AddMealUiState,
    uiActions: (AddMealUiAction) -> Unit,
    saveCompleted: Flow<Unit>,
    onNavigateBack: () -> Unit
) {
    val showDialog = rememberSaveable {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = Unit) {
        saveCompleted.collect {
            onNavigateBack()
        }
    }
    Scaffold(
        topBar = {
            TopBar(
                uiState = uiState,
                uiActions = uiActions,
                onNavigateBack = onNavigateBack
            )
        },
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember {
                MutableInteractionSource()
            }
        ) {
            focusManager.clearFocus()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            SearchModeSwitcher(searchMode = uiState.searchMode) {
                uiActions(AddMealUiAction.SetSearchMode(it))
            }
            when {
                uiState.searchBarText.isBlank() -> {
                    if (uiState.pickedFoodList.isEmpty()) StartScreen()
                    else PickedFoodListScreen(
                        uiState = uiState,
                        showDialog = showDialog,
                        uiActions = uiActions
                    )
                }
                uiState.searchBarText.isNotBlank() -> {
                    when (uiState.pagingDataFlow) {
                        null -> {  }
                        else -> PagingDataScreen(
                            uiState = uiState,
                            showDialog = showDialog,
                            uiActions = uiActions
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchModeSwitcher(
    searchMode: SearchMode,
    onModeSelected: (SearchMode) -> Unit,
) {
    val transition = updateTransition(targetState = searchMode, label = "")
    Row {
        SearchMode.entries.forEach { mode ->
            val selectedColor by transition.animateColor(
                transitionSpec = { tween(durationMillis = 300) }, label = ""
            ) { state ->
                if (mode == state)
                    MaterialTheme.colorScheme.outline
                else Color.Transparent
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) { onModeSelected(mode) }
            ) {
                Text(
                    text = stringResource(mode.textId),
                    style = MaterialTheme.typography.searchCheckbox,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = selectedColor
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(80.dp)
                .alpha(.2f),
            color = MaterialTheme.colorScheme.surfaceTint,
            strokeWidth = 4.dp,
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
private fun PagingDataScreen(
    uiState: AddMealUiState,
    uiActions: (AddMealUiAction) -> Unit,
    showDialog: MutableState<Boolean>
) {
    val foodList = uiState.pagingDataFlow!!.collectAsLazyPagingItems()
    when (foodList.loadState.refresh) {
        is LoadState.Error -> {
            val rawError = (foodList.loadState.refresh as LoadState.Error).error
            val error = when (rawError) {
                is HttpException -> stringResource(id = rawError.toHttpError().messageId)
                is IOException -> stringResource(id = rawError.toIOError().messageId)
                else -> stringResource(id = R.string.unknown_error)
            }
            Log.e("paging error", rawError.message.toString())
            ErrorScreen(error = error, onClick = { foodList.retry() })
        }
        is LoadState.Loading -> LoadingScreen()
        is LoadState.NotLoading -> {
            if (foodList.itemCount > 0) LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(count = foodList.itemCount) { index ->
                    val item = foodList[index]
                    if (item != null) {
                        FoodItemCard(
                            food = item,
                            onFoodItemClicked = {
                                uiActions(AddMealUiAction.PickFood(item))
                                showDialog.value = true
                            },
                            onLongClick = { },
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        )
                    }
                }
                item {
                    foodList.let {
                        when (it.loadState.append) {
                            is LoadState.Error -> PagingErrorIndicator {
                                foodList.retry()
                            }
                            LoadState.Loading -> PagingLoadingIndicator()
                            is LoadState.NotLoading -> {  }
                        }
                    }
                }
            } else FoundNothingScreen()
        }
    }
    if (showDialog.value)
        MassDialog(
            showDialog = showDialog,
            uiState = uiState,
            uiActions = uiActions
        )
}

@Composable
private fun PagingLoadingIndicator() {
    val transition = rememberInfiniteTransition(label = "")
    val alpha by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(750),
                repeatMode = RepeatMode.Reverse
            ),
            label = ""
    )
    val color = MaterialTheme.colorScheme.secondaryContainer
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(topStartPercent = 25, topEndPercent = 25))
            .alpha(alpha)
            .background(color)
    )
}

@Composable
private fun PagingErrorIndicator(onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 4.dp), contentAlignment = Alignment.Center){
        Text(
            text = stringResource(id = R.string.retry),
            style = MaterialTheme.typography.screenMessageSmall,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }
                ) { onClick() }
        )
    }
}

@Composable
private fun FoundNothingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.found_nothing),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.found_nothing), style = MaterialTheme.typography.screenMessageMedium)
        }
    }
}

@Composable
private fun ErrorScreen(
    error: String,
    onClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error, style = MaterialTheme.typography.screenMessageMedium)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.try_again),
                style = MaterialTheme.typography.screenMessageSmall,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) { onClick() }
            )
        }
    }
}

@Composable
private fun PickedFoodListScreen(
    uiState: AddMealUiState,
    showDialog: MutableState<Boolean>,
    uiActions: (AddMealUiAction) -> Unit
) {
    val items = uiState.pickedFoodList.entries.toList()
    val scope = rememberCoroutineScope()
    val deletedItems = remember {
        mutableStateListOf<Food>()
    }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(items, key = { it.key.id ?: it.key.hashCode() }) {
                AnimatedVisibility(
                    visible = it.key !in deletedItems,
                    exit = slideOutHorizontally() + fadeOut()
                ) {
                    PickedFoodListItem(
                        name = it.key.name,
                        mass = it.value,
                        onEdit = {
                            uiActions(AddMealUiAction.PickFood(it.key))
                            showDialog.value = true
                        },
                        onRemove = {
                            deletedItems.add(it.key)
                            scope.launch {
                                delay(300L)
                                uiActions(AddMealUiAction.RemoveFromPickedList(it.key))
                                deletedItems.remove(it.key)
                            }
                        },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
        NutrientsProgressBar(uiState = uiState)
    }
    if (showDialog.value)
        MassDialog(
            showDialog = showDialog,
            uiState = uiState,
            uiActions = uiActions
        )
}

@Composable
private fun StartScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(
            text = stringResource(id = R.string.add_meal_start_message),
            style = MaterialTheme.typography.screenMessageMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MassDialog(
    uiState: AddMealUiState,
    uiActions: (AddMealUiAction) -> Unit,
    showDialog: MutableState<Boolean>,
) {
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
    BasicAlertDialog(
        onDismissRequest = { showDialog.value = false }
    ) {
        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(10),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.wrapContentSize()
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = uiState.pickedFood?.name ?: "Oops ...",
                    style = MaterialTheme.typography.screenMessageLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(.75f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ){
                    BasicTextField(
                        value = TextFieldValue(
                            text = uiState.foodMass?.toString() ?: EMPTY_STRING,
                            selection = TextRange(uiState.foodMass.toString().length)
                        ),
                        onValueChange = {
                            uiActions(AddMealUiAction.OnMassInputChanged(it.text))
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.screenMessageMedium,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            showKeyboardOnFocus = true
                        ),
                        decorationBox = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                it()
                                Spacer(modifier = Modifier.height(2.dp))
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                            }
                        },
                        modifier = Modifier
                            .wrapContentSize()
                            .focusRequester(focusRequester)
                    )
                    Text(
                        text = stringResource(id = R.string.enter_mass),
                        style = MaterialTheme.typography.underlineHint
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { showDialog.value = false }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceTint,
                            modifier = Modifier.scale(.75f)
                        )
                    }
                    IconButton(onClick = {
                        if (uiState.foodMass != null) {
                            uiActions(AddMealUiAction.AddFoodToPickedList)
                            showDialog.value = false
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.confirm),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceTint,
                            modifier = Modifier.scale(.75f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    searchText: String,
    onInputClear: () -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = {
            onValueChange(it)
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(100),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        ),
        textStyle = MaterialTheme.typography.searchBarInput,
        trailingIcon = {
            AnimatedVisibility(
                visible = searchText.isNotBlank(),
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                IconButton(
                    onClick = { onInputClear() },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_from_list),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier.scale(0.35f)
                    )
                }
            }
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint
            )
        }
    )
}

@Composable
private fun PickedFoodListItem(
    name: String,
    mass: Int,
    onEdit: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.pickedFood,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = { onEdit() }, modifier = Modifier.size(25.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.scale(0.75f)
            )
        }
        Text(
            text = stringResource(id = R.string.mass, mass),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.pickedFood,
            modifier = Modifier.weight(0.25f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = { onRemove() }, modifier = Modifier.size(25.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.delete_from_list),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.scale(0.5f)
            )
        }
    }
}

@Composable
private fun NutrientsProgressBar(
    uiState: AddMealUiState,
    modifier: Modifier = Modifier
) {
    val colorsList = listOf(
        CustomTheme.colors.notAchievedColor,
        CustomTheme.colors.littleAchievedColor,
        CustomTheme.colors.almostAchievedColor,
        CustomTheme.colors.achievedColor
    )
    val totalProgress = uiState.let {
        (it.currentTotalCals + it.pickedFoodTotalCals) / it.targetCalories
    }.toFloat().coerceIn(0f, 1f)
    val currentProgress = (uiState.currentTotalCals.toFloat() / uiState.targetCalories).coerceIn(0f, 1f)
    val additionalProgress = (uiState.pickedFoodTotalCals.toFloat() / uiState.targetCalories).coerceIn(0f, 1f)
    var totalWidth by remember {
        mutableStateOf(0f)
    }
    val color = interpolateColors(totalProgress, colorsList)

    val sumCals = (uiState.currentTotalCals + uiState.pickedFoodTotalCals).format(0)
    val sumProtein = (uiState.currentTotalProtein + uiState.pickedFoodListTotalProtein).format(1)
    val sumFat = (uiState.currentTotalFat + uiState.pickedFoodListTotalFat).format(1)
    val sumCarbs = (uiState.currentTotalCarbs + uiState.pickedFoodListTotalCarbs).format(1)
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            text = sumCals + "/" + "${uiState.targetCalories}" + "(+${uiState.pickedFoodTotalCals.format(0)})",
            style = MaterialTheme.typography.screenMessageMedium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .onGloballyPositioned {
                    totalWidth = it.size.width.toFloat()
                }
        ) {
            val currentProgressWidth by animateFloatAsState(targetValue = totalWidth * currentProgress, label = "")
            val additionalProgressWidth by animateFloatAsState(targetValue = totalWidth * additionalProgress, label = "")
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = color,
                    size = Size(width = currentProgressWidth, height = size.height)
                )
                drawRect(
                    color = color.copy(alpha = .5f),
                    topLeft = Offset(x = currentProgressWidth, y = 0f),
                    size = Size(width = additionalProgressWidth, height = size.height)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = sumProtein + "(+${uiState.pickedFoodListTotalProtein.format(1)})",
                    style = MaterialTheme.typography.screenMessageSmall
                )
                Text(text = stringResource(id = R.string.protein), style = MaterialTheme.typography.underlineHint)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = sumFat + "(+${uiState.pickedFoodListTotalFat.format(1)})",
                    style = MaterialTheme.typography.screenMessageSmall
                )
                Text(text = stringResource(id = R.string.fat), style = MaterialTheme.typography.underlineHint)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = sumCarbs + "(+${uiState.pickedFoodListTotalCarbs.format(1)})",
                    style = MaterialTheme.typography.screenMessageSmall
                )
                Text(text = stringResource(id = R.string.carbs), style = MaterialTheme.typography.underlineHint)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    uiState: AddMealUiState,
    uiActions: (AddMealUiAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            SearchTextField(
                searchText = uiState.searchBarText,
                onInputClear = { uiActions(AddMealUiAction.ClearSearchBarInput) }
            ) {
                uiActions(AddMealUiAction.SearchBarTextChanged(it))
            }
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(.5f),
                    painter = painterResource(id = R.drawable.back),
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    contentDescription = null
                )
            }
        },
        actions = {
            AnimatedVisibility(
                visible = uiState.pickedFoodList.values.isNotEmpty() && (uiState.searchBarText.isBlank()),
                enter = expandHorizontally(expandFrom = Alignment.End),
                exit = shrinkHorizontally(shrinkTowards = Alignment.End)
            ) {
                IconButton(onClick = { uiActions(AddMealUiAction.ConfirmMeal) }) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(.5f),
                        painter = painterResource(id = R.drawable.confirm),
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        contentDescription = null
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    MassDialog(uiState = AddMealUiState(
        pickedFood = Food(
            name = "Pasta carbonara with sauce",
            protein = 2.0,
            fat = 3.0,
            carbs = 5.0,
            imageUrl = null
        ),
        foodMass = 100
    ), uiActions = {}, showDialog = mutableStateOf(true))
}