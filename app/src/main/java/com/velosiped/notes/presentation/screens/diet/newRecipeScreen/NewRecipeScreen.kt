package com.velosiped.notes.presentation.screens.diet.newRecipeScreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import com.velosiped.notes.R
import com.velosiped.notes.data.database.ingredient.Ingredient
import com.velosiped.notes.presentation.screens.diet.components.FoodLargeImage
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.ui.theme.inputFieldInput
import com.velosiped.notes.ui.theme.inputFieldNameSided
import com.velosiped.notes.ui.theme.topBarHeadline
import com.velosiped.notes.ui.theme.underlineHint
import com.velosiped.notes.utils.EMPTY_STRING
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun NewRecipeScreen(
    uiState: NewRecipeUiState,
    uiActions: (NewRecipeUiAction) -> Unit,
    operationCompleted: Flow<Unit>,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val ingredientsPage = 0
    val confirmationPage = 1
    val pageCount = 2
    val pagerState = rememberPagerState(initialPage = ingredientsPage, pageCount = {pageCount})
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = Unit) {
        if (uiState.generatedUri == null) uiActions(NewRecipeUiAction.CreateImageFile(context))
        operationCompleted.collect {
            onNavigateBack()
        }
    }
    fun backHandler() {
        when (pagerState.currentPage) {
            ingredientsPage -> uiState.generatedUri?.let { uiActions(NewRecipeUiAction.DeleteImageFile(context, it)) } ?: onNavigateBack()
            else -> scope.launch { pagerState.animateScrollToPage(ingredientsPage) }
        }
    }
    BackHandler {
        backHandler()
    }
    Scaffold(
        topBar = {
            TopBar(
                onNavigateBack = {
                    backHandler()
                },
                onConfirm = {
                    if (uiState.isValidFood && uiState.recipeName.isNotBlank()) {
                        uiActions(NewRecipeUiAction.ConfirmNewFood)
                    }
                },
                isVisible = uiState.isValidFood && (pagerState.currentPage == confirmationPage) && uiState.recipeName.isNotBlank()
            )
        },
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) {
                focusManager.clearFocus()
            }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ){
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    ingredientsPage -> IngredientsPage(
                        uiState = uiState,
                        uiActions = uiActions
                    )
                    confirmationPage -> ConfirmationPage(
                        uiState = uiState,
                        uiActions = uiActions,
                        context = context
                    )
                }
            }
            AnimatedVisibility(
                visible = uiState.isValidFood,
                enter = scaleIn(),
                exit = scaleOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 40.dp, bottom = 80.dp)
            ){
                FloatingActionButton(onClick = {
                    when (pagerState.currentPage) {
                        ingredientsPage -> scope.launch { pagerState.animateScrollToPage(confirmationPage) }
                        confirmationPage -> scope.launch { pagerState.animateScrollToPage(ingredientsPage) }
                    }
                },
                    shape = RoundedCornerShape(100),
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    val rotationAngle = animateFloatAsState(
                        targetValue = when (pagerState.currentPage) {
                            ingredientsPage -> 180f
                            else -> 0f
                        },
                        label = ""
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier.rotate(rotationAngle.value)
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfirmationPage(
    uiState: NewRecipeUiState,
    uiActions: (NewRecipeUiAction) -> Unit,
    context: Context
) {
    val toastText = stringResource(id = R.string.app_needs_camera_permission)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            uiActions(NewRecipeUiAction.UpdateImage)
        } // TODO: Если крашнуть приложение, то файл останется мертвым грузом
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission())
    { permissionGranted ->
        if (permissionGranted) {
            uiState.generatedUri?.let {
                cameraLauncher.launch(it)
            }
        } else {
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        FoodLargeImage(
            uri = uiState.imageUri,
            onClick = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    uiState.generatedUri?.let {
                        cameraLauncher.launch(it)
                    }
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    Log.e("permission", "request done")
                }
            },
            onDeletePic = { uiActions(NewRecipeUiAction.DeleteFoodImage) }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            InputField(
                text = uiState.recipeName,
                onValueChange = { uiActions(NewRecipeUiAction.OnRecipeNameChanged(it)) },
                keyboardType = KeyboardType.Unspecified
            )
            Text(
                text = stringResource(id = R.string.name),
                style = MaterialTheme.typography.underlineHint
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var isAutoMass by rememberSaveable {
                mutableStateOf(true)
            }
            MassRadioButton(selected = isAutoMass, text = stringResource(id = R.string.auto)) {
                isAutoMass = !isAutoMass
                uiActions(NewRecipeUiAction.ChangeMassSource(isAutoMass))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                InputField(
                    text = if (isAutoMass) uiState.autoCalculatedTotalMass.toString() else uiState.userDefinedTotalMass,
                    onValueChange = { uiActions(NewRecipeUiAction.OnRecipeMassChanged(it)) },
                    isReadOnly = isAutoMass,
                    showLockIcon = true,
                    modifier = Modifier.fillMaxWidth(.5f)
                )
                Text(
                    text = stringResource(id = R.string.mass_placeholder),
                    style = MaterialTheme.typography.underlineHint
                )
            }
            MassRadioButton(selected = !isAutoMass, text = stringResource(id = R.string.manual)) {
                isAutoMass = !isAutoMass
                uiActions(NewRecipeUiAction.ChangeMassSource(isAutoMass))
            }
        }
    }
}

@Composable
private fun MassRadioButton(
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        RadioButton(
            selected = selected, onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.surfaceTint,
                unselectedColor = MaterialTheme.colorScheme.surfaceTint
            )
        )
        Text(text = text, style = MaterialTheme.typography.underlineHint)
    }
}

@Composable
private fun IngredientsPage(
    uiState: NewRecipeUiState,
    uiActions: (NewRecipeUiAction) -> Unit
) {
    val ingredientsList = uiState.ingredientsList
    val deletedItems = remember {
        mutableStateListOf<IngredientInput>()
    }
    var isAnimationRunning by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(
                ingredientsList,
                key = { it.id }
            ) { ingredient ->
                AnimatedVisibility(
                    visible = ingredient !in deletedItems,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut()
                ) {
                    IngredientCard(
                        ingredient = ingredient,
                        uiActions = uiActions,
                        onDelete = {
                            if (isAnimationRunning) return@IngredientCard
                            else {
                                if (ingredient.isEmptyIngredient && ingredientsList.size > 1) {
                                    isAnimationRunning = true
                                    deletedItems.clear()
                                    deletedItems.add(ingredient)
                                    scope.launch {
                                        delay(300)
                                        uiActions(NewRecipeUiAction.DeleteIngredient(it))
                                        isAnimationRunning = false
                                    }
                                } else {
                                    uiActions(NewRecipeUiAction.DeleteIngredient(it))
                                }
                            }
                        },
                        ingredientsFound = uiState.ingredientsFoundList,
                        modifier = Modifier.animateItem(
                            fadeInSpec = tween(durationMillis = 300)
                        )
                    )
                }
            }
        }
        IngredientsCounter(
            uiState = uiState,
            onDecrease = {
                if (isAnimationRunning) return@IngredientsCounter
                else {
                    if (ingredientsList.size > 1) {
                        isAnimationRunning = true
                        val deletedIngredient = ingredientsList.last()
                        deletedItems.add(deletedIngredient)
                        scope.launch {
                            delay(300)
                            uiActions(NewRecipeUiAction.DecreaseNumberOfIngredients)
                            deletedItems.clear()
                            isAnimationRunning = false
                        }
                    }
                }
            },
            onIncrease = {
                if (isAnimationRunning) return@IngredientsCounter
                uiActions(NewRecipeUiAction.IncreaseNumberOfIngredients)
            }
        )
    }
}

@Composable
private fun IngredientsCounter(
    uiState: NewRecipeUiState,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 12.dp, top = 12.dp)
    ) {
        IconButton(
            onClick = { onDecrease() },
            modifier = Modifier.size(15.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.minus),
                contentDescription = null
            )
        }
        Text(
            text = uiState.numberOfIngredients.toString(),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
        IconButton(
            onClick = { onIncrease() },
            modifier = Modifier.size(15.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_plus),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun InputField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    textFieldAlignment: Alignment = Alignment.Center,
    textStyle: TextStyle = MaterialTheme.typography.inputFieldInput,
    keyboardType: KeyboardType = KeyboardType.Number,
    underlineHint: String = EMPTY_STRING,
    isReadOnly: Boolean = false,
    showLockIcon: Boolean = false,
    onUnderlineHeightMeasured: (Dp) -> Unit = {}
) {
    val localDensity = LocalDensity.current
    BasicTextField(
        value = text,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        cursorBrush = SolidColor(CustomTheme.colors.textSelectionHandleColor),
        decorationBox = { inputField ->
            val bgColor = if (isReadOnly) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
            Column(
                horizontalAlignment = alignment
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10))
                        .background(bgColor),
                    contentAlignment = textFieldAlignment
                ) {
                    inputField()
                    if (isReadOnly && showLockIcon) Icon(
                        painter = painterResource(id = R.drawable.lock),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                )
                Text(
                    text = underlineHint,
                    style = MaterialTheme.typography.underlineHint,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        val heightPx = coordinates.size.height
                        val height = with(localDensity) { heightPx.toDp() }
                        onUnderlineHeightMeasured(height)
                    }
                )
            }
        },
        readOnly = isReadOnly,
        modifier = modifier
    )
}

@Composable
private fun IngredientCard(
    ingredient: IngredientInput,
    ingredientsFound: List<Ingredient>,
    uiActions: (NewRecipeUiAction) -> Unit,
    onDelete: (IngredientInput) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val isReadOnly = ingredient.readOnly
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var inputWidth by remember {
        mutableIntStateOf(0)
    }
    var dropMenuOffset by remember {
        mutableStateOf(0.dp)
    }
    Card(
        shape = RoundedCornerShape(25),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, top = 12.dp, bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .padding(8.dp, bottom = 0.dp)
                        .weight(1f)
                ) {
                    InputField(
                        text = ingredient.name,
                        onValueChange = {
                            uiActions(NewRecipeUiAction.OnIngredientNameChanged(ingredient,it))
                            if (!isExpanded) isExpanded = true
                        },
                        alignment = Alignment.Start,
                        textFieldAlignment = Alignment.CenterStart,
                        keyboardType = KeyboardType.Unspecified,
                        underlineHint = stringResource(id = R.string.name),
                        textStyle = MaterialTheme.typography.inputFieldNameSided,
                        onUnderlineHeightMeasured = { dropMenuOffset = it },
                        isReadOnly = isReadOnly,
                        showLockIcon = true,
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            inputWidth = coordinates.size.width
                        }.onFocusChanged {
                            isExpanded = it.isFocused
                        },
                    )
                    if (ingredientsFound.isNotEmpty()) DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                        properties = PopupProperties(focusable = false),
                        offset = DpOffset(x = 0.dp, y = -dropMenuOffset),
                        modifier = Modifier
                            .width(with(LocalDensity.current) { inputWidth.toDp() })
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        ingredientsFound.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.name) },
                                onClick = {
                                    uiActions(NewRecipeUiAction.FillIngredientInfo(ingredient,it))
                                    focusManager.clearFocus()
                                    isExpanded = false
                                }
                            )
                        }
                    }
                }
                IconButton(
                    onClick = { onDelete(ingredient) },
                    modifier = Modifier
                        .weight(.15f)
                        .padding(bottom = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_from_list),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier.scale(.35f)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, top = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val weight = 1f
                val padding = 8.dp
                InputField(
                    text = ingredient.protein,
                    onValueChange = {
                        uiActions(NewRecipeUiAction.OnIngredientProteinChanged(ingredient,it))
                    },
                    underlineHint = stringResource(id = R.string.protein),
                    isReadOnly = isReadOnly,
                    modifier = Modifier
                        .weight(weight)
                        .padding(padding)
                )
                InputField(
                    text = ingredient.fat,
                    onValueChange = {
                        uiActions(NewRecipeUiAction.OnIngredientFatChanged(ingredient,it))
                    },
                    underlineHint = stringResource(id = R.string.fat),
                    isReadOnly = isReadOnly,
                    modifier = Modifier
                        .weight(weight)
                        .padding(padding)
                )
                InputField(
                    text = ingredient.carbs,
                    onValueChange = {
                        uiActions(NewRecipeUiAction.OnIngredientCarbsChanged(ingredient,it))
                    },
                    underlineHint = stringResource(id = R.string.carbs),
                    isReadOnly = isReadOnly,
                    modifier = Modifier
                        .weight(weight)
                        .padding(padding)
                )
                InputField(
                    text = ingredient.mass,
                    onValueChange = {
                        uiActions(NewRecipeUiAction.OnIngredientMassChanged(ingredient,it))
                    },
                    underlineHint = stringResource(id = R.string.mass_placeholder),
                    modifier = Modifier
                        .weight(weight)
                        .padding(padding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    isVisible: Boolean,
    onNavigateBack: () -> Unit,
    onConfirm: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = stringResource(id = R.string.new_recipe_headline),
                style = MaterialTheme.typography.topBarHeadline,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(.5f),
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null
                )
            }
        },
        actions = {
            AnimatedContent(targetState = isVisible, label = "") { visible ->
                if (visible) IconButton(onClick = { onConfirm() }) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(.5f),
                        painter = painterResource(id = R.drawable.confirm),
                        contentDescription = null
                    )
                } else {
                    Box(modifier = Modifier.size(48.dp))
                }
            }
        }
    )
}