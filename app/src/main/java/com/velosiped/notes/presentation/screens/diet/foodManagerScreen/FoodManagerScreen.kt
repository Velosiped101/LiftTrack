package com.velosiped.notes.presentation.screens.diet.foodManagerScreen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.toUri
import com.velosiped.notes.R
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.presentation.screens.components.fooditem.FoodImage
import com.velosiped.notes.presentation.screens.components.fooditem.FoodItemCard
import com.velosiped.notes.ui.theme.CustomTheme
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Composable
fun FoodManagerScreen(
    uiState: FoodManagerUiState,
    uiActions: (FoodManagerUiAction) -> Unit,
    loadingFinished: SharedFlow<Unit>,
    onNavigateBack: () -> Unit
) {
    var isNotLoading by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = loadingFinished) {
        loadingFinished.collect {
            isNotLoading = true
        }
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val mainPage = 0
    val inputPage = 1
    val pageCount = 2
    val pagerState = rememberPagerState(initialPage = mainPage, pageCount = { pageCount })
    val toastText = stringResource(id = R.string.app_needs_camera_permission)
    val focusManager: FocusManager = LocalFocusManager.current

    fun handleBack() {
        focusManager.clearFocus()
        if (pagerState.currentPage == inputPage) {
            uiState.generatedUri?.let {
                uiActions(FoodManagerUiAction.DeleteImageFile(context, it.toString()))
            }
            scope.launch { pagerState.animateScrollToPage(mainPage) }
        } else {
            if (uiState.isInDeleteMode) uiActions(FoodManagerUiAction.ExitDeleteMode)
            else onNavigateBack()
        }
    }
    BackHandler { handleBack() }
    Scaffold(
        topBar = {
            TopBar(
                onAddButtonClicked = {
                    uiActions(FoodManagerUiAction.CreateImageFile(context))
                    uiActions(FoodManagerUiAction.GetFoodInformation(null))
                    scope.launch { pagerState.animateScrollToPage(inputPage) }
                },
                onSaveButtonClicked = {
                    focusManager.clearFocus()
                    if (uiState.isFoodInputValid) {
                        uiActions(FoodManagerUiAction.OnConfirmDialog)
                        scope.launch { pagerState.animateScrollToPage(mainPage) }
                    }
                },
                onBackButtonClicked = { handleBack() },
                isOnMainPage = pagerState.currentPage == mainPage,
                isInDeleteMode = uiState.isInDeleteMode,
                onDelete = { uiActions(FoodManagerUiAction.DeleteFood(context)) }
            )
        }
    ) { innerPadding ->
        AnimatedContent(targetState = isNotLoading, label = "") {
            when (it) {
                true -> HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                    modifier = Modifier.padding(innerPadding)
                ) { page ->
                    when (page) {
                        mainPage -> MainPage(
                            uiState = uiState,
                            onFoodItemClicked = { item ->
                                uiActions(FoodManagerUiAction.OnFoodClick(item))
                                if (!uiState.isInDeleteMode) {
                                    uiActions(FoodManagerUiAction.CreateImageFile(context))
                                    scope.launch { pagerState.animateScrollToPage(inputPage) }
                                }
                            },
                            onLongClick = { item ->
                                uiActions(FoodManagerUiAction.OnFoodLongClick(item))
                            }
                        )
                        inputPage -> InputPage(
                            uiState = uiState,
                            uiActions = uiActions,
                            context = context,
                            toastText = toastText
                        )
                    }
                }
                false -> {  }
            }
        }
    }
}

@Composable
private fun InputPage(
    uiState: FoodManagerUiState,
    uiActions: (FoodManagerUiAction) -> Unit,
    context: Context,
    toastText: String
) {
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            uiActions(FoodManagerUiAction.UpdateImageFile(context))
        } else {
            uiActions(FoodManagerUiAction.DeleteImageFile(context,
                (uiState.generatedUri ?: return@rememberLauncherForActivityResult).toString()
            ))
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            uiState.generatedUri?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        }
    }
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val focusManager: FocusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .imePadding()
            .verticalScroll(scrollState)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FoodImage(
            uri = uiState.pickedFoodInput.imageUri?.toUri(),
//            onClick = {
//                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                    uiState.generatedUri?.let { cameraLauncher.launch(it) }
//                } else {
//                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
//                }
//            },
//            onDeletePic = { uiActions(FoodManagerUiAction.DeleteFoodPicture) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputField(
            inputText = uiState.pickedFoodInput.name,
            fieldName = stringResource(id = R.string.name),
            onValueChange = { uiActions(FoodManagerUiAction.OnFoodNameChanged(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            focusRequester = focusRequester
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            InputField(
                inputText = uiState.pickedFoodInput.protein,
                fieldName = stringResource(id = R.string.protein),
                onValueChange = { uiActions(FoodManagerUiAction.OnFoodProteinChanged(it)) },
                focusRequester = focusRequester,
                modifier = Modifier.weight(1f)
            )
            InputField(
                inputText = uiState.pickedFoodInput.fat,
                fieldName = stringResource(id = R.string.fat),
                onValueChange = { uiActions(FoodManagerUiAction.OnFoodFatChanged(it)) },
                focusRequester = focusRequester,
                modifier = Modifier.weight(1f)
            )
            InputField(
                inputText = uiState.pickedFoodInput.carbs,
                fieldName = stringResource(id = R.string.carbs),
                onValueChange = { uiActions(FoodManagerUiAction.OnFoodCarbsChanged(it)) },
                focusRequester = focusRequester,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MainPage(
    uiState: FoodManagerUiState,
    onFoodItemClicked: (Food) -> Unit,
    onLongClick: (Food) -> Unit,
) {
    AnimatedContent(targetState = uiState.foodList.isNotEmpty(), label = "") { targetState ->
        when (targetState) {
            true -> LazyColumn(
                contentPadding = PaddingValues(2.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.foodList.sortedBy { it.name }) { item ->
                    val colors = if (uiState.selectedForDeleteList.contains(item))
                        CardDefaults.cardColors(containerColor = CustomTheme.colors.listItemColors.markedForDeleteColor)
                    else CardDefaults.cardColors(containerColor = CustomTheme.colors.listItemColors.containerColor)
                    FoodItemCard(
                        food = item,
                        onFoodItemClicked = { onFoodItemClicked(item) },
                        onLongClick = { onLongClick(item) }
                    )
                }
            }
            false -> Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.empty_food_menu),
                        contentDescription = null,
                        tint = CustomTheme.colors.iconsTintColor,
                        modifier = Modifier.fillMaxWidth(.5f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(id = R.string.empty_food_menu_message),
                        style = CustomTheme.typography.screenMessageMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.empty_food_menu_message_hint),
                        style = CustomTheme.typography.screenMessageSmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InputField(
    modifier: Modifier = Modifier,
    inputText: String,
    onValueChange: (String) -> Unit,
    fieldName: String,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = inputText,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            singleLine = true,
            modifier = Modifier
                .wrapContentHeight()
                .bringIntoViewRequester(bringIntoViewRequester)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        scope.launch { bringIntoViewRequester.bringIntoView() }
                    }
                },
            decorationBox = { textField ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    textField()
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(color = CustomTheme.colors.dividerColor)
                }
            },
            textStyle = CustomTheme.typography.textFieldInput
        )
        Text(
            text = fieldName,
            style = CustomTheme.typography.underlineHint
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onAddButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit,
    isOnMainPage: Boolean,
    isInDeleteMode: Boolean,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = {
            AnimatedVisibility(
                visible = isOnMainPage,
                enter = slideInHorizontally() + fadeIn(),
                exit = slideOutHorizontally() + fadeOut()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.food_manager_headline),
                        style = CustomTheme.typography.topBarHeadline,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(0.5f),
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null,
                    tint = CustomTheme.colors.iconsTintColor
                )
            }
        },
        actions = {
            Crossfade(
                targetState = Pair(isInDeleteMode, isOnMainPage),
                label = "topBarCrossFade"
            ) { (inDeleteMode, onMainPage) ->
                when {
                    onMainPage -> {
                        if (inDeleteMode) {
                            IconButton(onClick = onDelete) {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = R.drawable.delete),
                                    contentDescription = null
                                )
                            }
                        } else {
                            IconButton(onClick = onAddButtonClicked) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .scale(0.5f),
                                    painter = painterResource(id = R.drawable.add_plus),
                                    contentDescription = null,
                                    tint = CustomTheme.colors.iconsTintColor
                                )
                            }
                        }
                    }
                    else -> IconButton(onClick = onSaveButtonClicked) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(0.5f),
                            painter = painterResource(id = R.drawable.confirm),
                            contentDescription = null,
                            tint = CustomTheme.colors.iconsTintColor
                        )
                    }
                }
            }
        }
    )
}