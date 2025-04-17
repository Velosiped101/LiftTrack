package com.example.notes.presentation.screens.diet.addMealScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.R
import com.example.notes.presentation.screens.diet.components.FoodItemCard
import com.example.notes.utils.EMPTY_STRING

@Composable
fun AddMealScreen(
    uiState: AddMealUiState,
    uiActions: (AddMealUiAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    val isDialogActive = rememberSaveable {
        mutableStateOf(false)
    }
    BackHandler {
        uiActions(AddMealUiAction.ResetUi)
        onNavigateBack()
    }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            TopBar(
                uiState = uiState,
                uiActions = uiActions,
                onNavigateBack = onNavigateBack
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SearchCheckbox(
                    text = "Local",
                    checkboxState = uiState.getFromLocal,
                    modifier = Modifier.weight(1f),
                    onCheckedChange = { uiActions(AddMealUiAction.SearchInLocal) }
                )
                SearchCheckbox(
                    text = "Remote",
                    checkboxState = uiState.getFromRemote,
                    modifier = Modifier.weight(1f),
                    onCheckedChange = { uiActions(AddMealUiAction.SearchInRemote) }
                )
            }
            when (uiState.holder) {
                is FoodHolder.Start ->
                    if (uiState.pickedFoodList.isEmpty()) StartScreen()
                    else PickedFoodListScreen(
                        uiState = uiState,
                        isDialogActive = isDialogActive,
                        uiActions = uiActions
                    )
                is FoodHolder.TextChange -> {}
                is FoodHolder.Loading -> LoadingScreen()
                is FoodHolder.Success -> SuccessScreen(
                    uiState = uiState,
                    isDialogActive = isDialogActive,
                    uiActions = uiActions
                )
                is FoodHolder.Error -> ErrorScreen(
                    error = uiState.holder.throwable?.message ?: "unknown error"
                )
            }
        }
    }
}

@Composable
private fun SearchCheckbox(
    text: String,
    checkboxState: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isChecked by remember {
        mutableStateOf(checkboxState)
    }
    Card(
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center
            )
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    onCheckedChange()
                    isChecked = !isChecked
                }
            )
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
            color = Color.Gray,
            strokeWidth = 4.dp,
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
private fun SuccessScreen(
    isDialogActive: MutableState<Boolean>,
    uiState: AddMealUiState,
    uiActions: (AddMealUiAction) -> Unit
) {
    val foodList = uiState.holder.data
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (!foodList.isNullOrEmpty()) {
            items(foodList) { item ->
                FoodItemCard(
                    food = item,
                    onFoodItemClicked = {
                        uiActions(AddMealUiAction.PickFood(item))
                        isDialogActive.value = true
                    },
                    onLongClick = { },
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                )
            }
        }
        else item {
            Text(text = "Found nothing")
        }
    }
    if (isDialogActive.value)
        MassDialog(
            isDialogActive = isDialogActive,
            uiState = uiState,
            uiActions = uiActions
        )
}

@Composable
private fun ErrorScreen(
    error: String,
    modifier: Modifier = Modifier,
) {
    Text(text = error)
}

@Composable
private fun PickedFoodListScreen(
    uiState: AddMealUiState,
    isDialogActive: MutableState<Boolean>,
    uiActions: (AddMealUiAction) -> Unit
) {
    LazyColumn {
        items(uiState.pickedFoodList.entries.toMutableStateList()) {
            PickedFoodListItem(
                name = it.key.name,
                mass = it.value,
                onEdit = {
                    uiActions(AddMealUiAction.PickFood(it.key))
                    isDialogActive.value = true
                }
            ) {
                uiActions(AddMealUiAction.RemoveFromPickedList(it.key))
            }
        }
    }
    if (isDialogActive.value)
        MassDialog(
            isDialogActive = isDialogActive,
            uiState = uiState,
            uiActions = uiActions
        )
}

@Composable
private fun StartScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Use a search above to add your meal",
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MassDialog(
    uiState: AddMealUiState,
    uiActions: (AddMealUiAction) -> Unit,
    isDialogActive: MutableState<Boolean>,
) {
    BasicAlertDialog(
        onDismissRequest = {
            isDialogActive.value = false
        }
    ) {
        val focusRequester = remember {
            FocusRequester()
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Card(
            Modifier.background(Color.Transparent)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Enter mass in grams")
                BasicTextField(
                    value = TextFieldValue(
                        text = uiState.foodMass?.toString() ?: EMPTY_STRING,
                        selection = TextRange(uiState.foodMass.toString().length)
                    ),
                    onValueChange = {
                        uiActions(AddMealUiAction.OnMassInputChanged(it.text))
                    },
                    singleLine = true,
                    modifier = Modifier
                        .height(50.dp)
                        .wrapContentSize()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        showKeyboardOnFocus = true
                    )
                )
                Button(
                    onClick = {
                        uiActions(AddMealUiAction.AddFoodToPickedList)
                        isDialogActive.value = false
                    },
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = "Confirm",
                        color = if (uiState.isMassValid) MaterialTheme.colorScheme.onSecondaryContainer
                        else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    searchText: String,
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
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black
        ),
        textStyle = TextStyle(
            fontSize = 24.sp
        )
    )
}

@Composable
private fun PickedFoodListItem(
    name: String,
    mass: Int,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "$mass g", textAlign = TextAlign.End)
            Icon(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = null,
                Modifier
                    .clickable { onEdit() }
                    .size(25.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.delete_from_list),
                contentDescription = null,
                Modifier
                    .clickable { onRemove() }
                    .size(25.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    uiState: AddMealUiState,
    uiActions: (AddMealUiAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            SearchTextField(
                searchText = uiState.searchBarText
            ) {
                uiActions(AddMealUiAction.Search(it))
            }
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        onNavigateBack()
                        uiActions(AddMealUiAction.ResetUi)
                    },
                painter = painterResource(id = R.drawable.back),
                contentDescription = null
            )
        },
        actions = {
            AnimatedVisibility(
                visible = uiState.pickedFoodList.values.isNotEmpty() && (uiState.holder is FoodHolder.Start),
                enter = expandHorizontally(expandFrom = Alignment.End),
                exit = shrinkHorizontally(shrinkTowards = Alignment.End)
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            uiActions(AddMealUiAction.ConfirmMeal)
                        },
                    painter = painterResource(id = R.drawable.confirm),
                    contentDescription = null
                )
            }
        }
    )
}