package com.example.notes.presentation.screens.diet.newRecipeScreen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.R
import com.example.notes.ui.theme.ingredientNameInput
import com.example.notes.ui.theme.ingredientNutrientInput
import com.example.notes.ui.theme.underlineHint

@Composable
fun NewRecipeScreen(
    uiState: NewRecipeUiState,
    uiActions: (NewRecipeUiAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDialogActive = rememberSaveable {
        mutableStateOf(false)
    }
    BackHandler {
        onNavigateBack()
    }
    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                onNavigateBack = onNavigateBack,
                onConfirm = {
                    uiActions(NewRecipeUiAction.ConfirmNewFood)
                    onNavigateBack()
                },
                onWeighing = { isDialogActive.value = true },
                mass = uiState.totalMassCalc,
                name = uiState.recipeName,
                onNameChange = { uiActions(NewRecipeUiAction.OnRecipeNameChanged(it)) }
            )
            IngredientsTable(
                uiState = uiState,
                uiActions = uiActions,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .weight(1f),
            )
            IngredientsCounter(
                uiState = uiState,
                uiActions = uiActions
            )
        }
    }
    if (isDialogActive.value) MassDialog(
        isDialogActive = isDialogActive,
        uiState = uiState,
        uiActions = uiActions
    )
}

@Composable
fun IngredientsCounter(
    uiState: NewRecipeUiState,
    uiActions: (NewRecipeUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 12.dp, top = 12.dp)
    ) {
        IconButton(
            onClick = { uiActions(NewRecipeUiAction.DecreaseNumberOfIngredients) },
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
            onClick = { uiActions(NewRecipeUiAction.IncreaseNumberOfIngredients) },
            modifier = Modifier.size(15.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_plus),
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MassDialog(
    isDialogActive: MutableState<Boolean>,
    uiState: NewRecipeUiState,
    uiActions: (NewRecipeUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(onDismissRequest = { isDialogActive.value = false }) {
        Card(
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Name")
                Spacer(modifier = Modifier.height(12.dp))
                InputField(
                    text = uiState.recipeName,
                    modifier = Modifier,
                    onValueChange = { uiActions(NewRecipeUiAction.OnRecipeNameChanged(it)) },
                    keyboardOptions = KeyboardOptions.Default,
                    textStyle = MaterialTheme.typography.ingredientNameInput
                )
                Text(text = "Mass of the food")
                Spacer(modifier = Modifier.height(12.dp))
                InputField(
                    text = uiState.totalMass.toString(),
                    modifier = Modifier,
                    onValueChange = { uiActions(NewRecipeUiAction.OnRecipeMassChanged(it)) },
                    textStyle = MaterialTheme.typography.ingredientNutrientInput
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { isDialogActive.value = false }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {
                        isDialogActive.value = false
                        uiActions(NewRecipeUiAction.ConfirmNewFood)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.confirm),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IngredientsTable(
    uiState: NewRecipeUiState,
    uiActions: (NewRecipeUiAction) -> Unit,
    modifier: Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(uiState.ingredientsList) { id, ingredient ->
            IngredientCard(id = id, ingredient = ingredient, uiActions = uiActions)
        }
    }
}

@Composable
private fun InputField(
    text: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number
    ),
    placeholder: String = "",
    textStyle: TextStyle,
    modifier: Modifier
) {
    BasicTextField(
        value = text,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        decorationBox = { inputField ->
            Column {
                Box {
                    if (text.isBlank()) Text(
                        text = placeholder,
                        style = textStyle,
                        modifier = Modifier.alpha(.3f)
                    )
                    inputField()
                }
                Box {
                    if (text.isNotBlank()) Text(
                        text = placeholder.lowercase(),
                        style = MaterialTheme.typography.underlineHint
                    ) else
                    Text(
                        text = ""
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
    )
}

@Composable
private fun IngredientCard(
    id: Int,
    ingredient: IngredientInput,
    uiActions: (NewRecipeUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(25),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start
            ) {
                InputField(
                    text = ingredient.name,
                    onValueChange = { text ->
                        uiActions(NewRecipeUiAction.OnIngredientNameChanged(id, text))
                    },
                    modifier = Modifier.padding(2.dp),
                    keyboardOptions = KeyboardOptions.Default,
                    placeholder = "IngredientInput",
                    textStyle = MaterialTheme.typography.ingredientNameInput
                )
                Row(
                    modifier = Modifier.padding(2.dp)
                ) {
                    val width = 85.dp
                    InputField(
                        text = ingredient.protein,
                        onValueChange = { text ->
                            uiActions(NewRecipeUiAction.OnIngredientProteinChanged(id, text))
                        },
                        modifier = Modifier.width(width),
                        placeholder = "Protein, g",
                        textStyle = MaterialTheme.typography.ingredientNutrientInput
                    )
                    InputField(
                        text = ingredient.fat,
                        onValueChange = { text ->
                            uiActions(NewRecipeUiAction.OnIngredientFatChanged(id, text))
                        },
                        modifier = Modifier.width(width),
                        placeholder = "Fat, g",
                        textStyle = MaterialTheme.typography.ingredientNutrientInput
                    )
                    InputField(
                        text = ingredient.carbs,
                        onValueChange = { text ->
                            uiActions(NewRecipeUiAction.OnIngredientCarbsChanged(id, text))
                        },
                        modifier = Modifier.width(width),
                        placeholder = "Carbs, g",
                        textStyle = MaterialTheme.typography.ingredientNutrientInput
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(.3f)
                    .fillMaxSize()
            ) {
                IconButton(
                    onClick = { uiActions(NewRecipeUiAction.DeleteIngredient(id)) },
                    modifier = Modifier
                        .padding(top = 10.dp, end = 10.dp)
                        .size(15.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_from_list),
                        contentDescription = null
                    )
                }
                InputField(
                    text = ingredient.mass,
                    onValueChange = { text ->
                        uiActions(NewRecipeUiAction.OnIngredientMassChanged(id, text))
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    placeholder = "Mass, g",
                    textStyle = MaterialTheme.typography.ingredientNutrientInput
                )
            }
        }
    }
}

@Composable
private fun Divider(modifier: Modifier = Modifier) {
    VerticalDivider(
        modifier = Modifier
            .height(30.dp)
            .padding(top = 2.dp, bottom = 2.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onNavigateBack: () -> Unit,
    onConfirm: () -> Unit,
    onWeighing: () -> Unit,
    onNameChange: (String) -> Unit,
    mass: Int,
    name: String,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            BasicTextField(
                value = name,
                onValueChange = { onNameChange(it) },
                textStyle = LocalTextStyle.current,
                modifier = Modifier.fillMaxWidth()
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
            IconButton(onClick = { onWeighing() }){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = mass.toString())
                    Icon(
                        modifier = Modifier
                            .fillMaxSize(),
                        painter = painterResource(id = R.drawable.weighing_machine),
                        contentDescription = null
                    )
                }
            }
            IconButton(onClick = { onConfirm() }) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(.5f),
                    painter = painterResource(id = R.drawable.confirm),
                    contentDescription = null
                )
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview (showSystemUi = true)
@Composable
private fun Preview() {
    NewRecipeScreen(
        uiState = NewRecipeUiState(ingredientsList = mutableStateListOf(IngredientInput(name = "trulala"))),
        uiActions = {  },
        onNavigateBack = {  }
    )
}