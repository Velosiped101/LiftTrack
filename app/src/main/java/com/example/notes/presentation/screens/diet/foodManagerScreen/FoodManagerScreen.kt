package com.example.notes.presentation.screens.diet.foodManagerScreen

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.toCoilUri
import com.example.notes.R
import com.example.notes.presentation.screens.diet.components.FoodItemCard
import com.example.notes.ui.theme.input
import com.example.notes.utils.EMPTY_STRING
import kotlinx.coroutines.flow.update
import java.io.File

@Composable
fun FoodManagerScreen(
    uiState: FoodManagerUiState,
    uiActions: (FoodManagerUiAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    val isDialogActive = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    BackHandler {
        if (uiState.isInDeleteMode) uiActions(FoodManagerUiAction.ExitDeleteMode)
        else onNavigateBack()
    }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            TopBar(
                onAddButtonClicked = {
                    uiActions(FoodManagerUiAction.GetFoodInformation(null))
                    isDialogActive.value = true
                },
                onBackButtonClicked = {
                    if (uiState.isInDeleteMode) uiActions(FoodManagerUiAction.ExitDeleteMode)
                    else onNavigateBack()
                },
                isInDeleteMode = uiState.isInDeleteMode,
                onDelete = {
                    uiActions(FoodManagerUiAction.DeleteFood(context))
                }
            )
            LazyColumn {
                items(uiState.foodList) { item ->
                    val colors = if (uiState.selectedForDeleteList.contains(item))
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    else CardDefaults.cardColors(containerColor = Color.Transparent)
                    FoodItemCard(
                        food = item,
                        onFoodItemClicked = {
                            uiActions(FoodManagerUiAction.OnFoodClick(item))
                            if (!uiState.isInDeleteMode) isDialogActive.value = true
                        },
                        onLongClick = {
                            uiActions(FoodManagerUiAction.OnFoodLongClick(item))
                        },
                        colors = colors
                    )
                }
            }
        }
    }
    if (isDialogActive.value)
        EditFoodDialog(
            isDialogActive = isDialogActive,
            uiState = uiState,
            uiActions = uiActions,
            context = context
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditFoodDialog(
    isDialogActive: MutableState<Boolean>,
    uiState: FoodManagerUiState,
    uiActions: (FoodManagerUiAction) -> Unit,
    context: Context,
    modifier: Modifier = Modifier
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            uiActions(FoodManagerUiAction.UpdateImage)
        } else {
            uiActions(FoodManagerUiAction.DeleteImageFile(context = context, uri = uiState.generatedUri ?: return@rememberLauncherForActivityResult))
        }
    }
    BasicAlertDialog(onDismissRequest = {
        isDialogActive.value = false
    }) {
        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FoodImage(
                    uri = uiState.pickedFood.imageUri?.toUri(),
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds()
                        .padding(8.dp),
                    onDeletePic = {
                        uiActions(FoodManagerUiAction.DeleteFoodImage)
                    },
                    onClick = {
                        uiActions(FoodManagerUiAction.CreateImageFile(context))
                        launcher.launch(uiState.generatedUri ?: return@FoodImage)
                    }
                )
                InputField(
                    inputText = uiState.pickedFood.name,
                    fieldName = "Name",
                    onValueChange = { uiActions(FoodManagerUiAction.OnFoodNameChanged(it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                Row {
                    InputField(
                        inputText = uiState.pickedFood.protein,
                        fieldName = "Protein",
                        onValueChange = { uiActions(FoodManagerUiAction.OnFoodProteinChanged(it)) },
                        modifier = Modifier.weight(1f)
                    )
                    InputField(
                        inputText = uiState.pickedFood.fat,
                        fieldName = "Fat",
                        onValueChange = { uiActions(FoodManagerUiAction.OnFoodFatChanged(it)) },
                        modifier = Modifier.weight(1f)
                    )
                    InputField(
                        inputText = uiState.pickedFood.carbs,
                        fieldName = "Carbs",
                        onValueChange = { uiActions(FoodManagerUiAction.OnFoodCarbsChanged(it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { isDialogActive.value = false }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                    IconButton(onClick = {
                            uiActions(FoodManagerUiAction.OnConfirmDialog)
                            isDialogActive.value = false
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.confirm),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InputField(
    modifier: Modifier = Modifier,
    inputText: String,
    onValueChange: (String) -> Unit,
    fieldName: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next
    )
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = inputText,
            onValueChange = {
                onValueChange(it)
            },
            keyboardOptions = keyboardOptions,
            singleLine = true,
            modifier = Modifier.wrapContentHeight(),
            decorationBox = { textField ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    textField()
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            },
            textStyle = MaterialTheme.typography.input.copy(color = MaterialTheme.colorScheme.onSecondaryContainer)
        )
        Text(
            text = fieldName,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun FoodImage(
    modifier: Modifier = Modifier,
    uri: Uri?,
    onClick: () -> Unit,
    onDeletePic: () -> Unit
) {
    val size = if (uri == null) 200.dp else 300.dp
    Box(
        modifier = Modifier
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        uri.let {
            when (it) {
                null -> Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = null,
                    modifier = modifier
                        .clip(RoundedCornerShape(20))
                        .scale(.25f)
                        .clickable {
                            onClick()
                        },
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
                else -> AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(uri?.toCoilUri())
                        .build(),
                    contentDescription = null,
                    modifier = modifier
                        .clip(RoundedCornerShape(20f)),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
        if (uri != null)
        IconButton(
            onClick = { onDeletePic() },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.delete_from_list),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onAddButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    isInDeleteMode: Boolean,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Food manager",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onBackButtonClicked
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(0.5f),
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
            }
        },
        actions = {
            if (!isInDeleteMode) {
                IconButton(onClick = onAddButtonClicked) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(0.5f),
                        painter = painterResource(id = R.drawable.add_plus),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                }
            }
            else
                IconButton(onClick = onDelete) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = null
                    )
                }
        }
    )
}