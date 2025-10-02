package com.velosiped.newrecipe.presentation.components.editpage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.velosiped.diet.ingredient.repository.Ingredient
import com.velosiped.newrecipe.R
import com.velosiped.ui.components.CustomTextField
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ZERO

@Composable
fun IngredientExpandableTextField(
    textFieldState: TextFieldState,
    dropMenuItems: List<Ingredient>,
    readOnly: Boolean,
    onFocusChanged: (FocusState) -> Unit,
    onDropMenuItemClick: (Ingredient) -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    var inputWidth by remember { mutableIntStateOf(Int.ZERO) }
    var dropMenuOffset by remember { mutableStateOf(Int.ZERO.dp) }
    CustomTextField(
        value = textFieldState.text,
        onValueChange = { onValueChange(it) },
        underlineHint = stringResource(id = R.string.new_recipe_ingredient_name),
        readOnly = readOnly,
        alignToStart = true,
        modifier = modifier
            .onGloballyPositioned { inputWidth = it.size.width }
            .onFocusChanged {
                isFocused = it.isFocused
                onFocusChanged(it)
            }
    )
    if (dropMenuItems.isNotEmpty() && isFocused && !readOnly) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = {},
            properties = PopupProperties(focusable = false),
            offset = DpOffset(x = Int.ZERO.dp, y = -dropMenuOffset),
            modifier = Modifier.width(with(LocalDensity.current) { inputWidth.toDp() })
        ) {
            dropMenuItems.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = { onDropMenuItemClick(it) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandedPreview() {
    val dropMenuItems = listOf(
        Ingredient(name = "carrot", protein = 1f, fat = 1f, carbs = 1f),
        Ingredient(name = "low carb bread", protein = 1f, fat = 1f, carbs = 1f),
        Ingredient(name = "car tuna", protein = 1f, fat = 1f, carbs = 1f)
    )
    CustomTheme {
        IngredientExpandableTextField(
            textFieldState = TextFieldState(text = "car"),
            dropMenuItems = dropMenuItems,
            readOnly = false,
            onFocusChanged = {  },
            onDropMenuItemClick = {  },
            onValueChange = {  },
            modifier = Modifier.fillMaxSize()
        )
    }
}