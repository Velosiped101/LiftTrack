package com.velosiped.notes.presentation.screens.diet.foodManagerScreen.components.editpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.fooditem.FoodImage
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.FoodManagerUiState.FoodInput
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun FoodManagerEditPage(
    food: FoodInput,
    onNameChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onFatChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val focusManager: FocusManager = LocalFocusManager.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_8)),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
    ) {
        FoodImage(
            uri = food.imageUri,
            modifier = Modifier.clickable { onImageClick() }
        )
        FoodManagerFoodEditFields(
            food = food,
            onNameChange = onNameChange,
            onProteinChange = onProteinChange,
            onFatChange = onFatChange,
            onCarbsChange = onCarbsChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        FoodManagerEditPage(
            food = FoodInput(name = "Something", carbs = "56.0"),
            onNameChange = { },
            onProteinChange = { },
            onFatChange = { },
            onCarbsChange = { },
            onImageClick = { },
            modifier = Modifier.fillMaxSize()
        )
    }
}