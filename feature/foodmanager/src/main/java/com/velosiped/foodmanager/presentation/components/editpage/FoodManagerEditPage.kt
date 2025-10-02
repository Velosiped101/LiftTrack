package com.velosiped.foodmanager.presentation.components.editpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.velosiped.foodmanager.presentation.utils.FoodInputState
import com.velosiped.ui.R as coreR
import com.velosiped.ui.components.image.FoodImage
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.ui.theme.CustomTheme

@Composable
fun FoodManagerEditPage(
    foodInputState: FoodInputState,
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
        verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_8)),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
    ) {
        FoodImage(
            uri = foodInputState.imageUri,
            modifier = Modifier.clickable { onImageClick() }
        )
        FoodManagerFoodEditFields(
            foodInputState = foodInputState,
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
            foodInputState = FoodInputState(
                nameFieldState = TextFieldState(text = "new food")
            ),
            onNameChange = { },
            onProteinChange = { },
            onFatChange = { },
            onCarbsChange = { },
            onImageClick = { },
            modifier = Modifier.fillMaxSize()
        )
    }
}