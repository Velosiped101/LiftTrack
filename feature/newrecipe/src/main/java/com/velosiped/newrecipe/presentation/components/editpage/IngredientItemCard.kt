package com.velosiped.newrecipe.presentation.components.editpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.diet.ingredient.repository.Ingredient
import com.velosiped.newrecipe.R
import com.velosiped.newrecipe.presentation.components.utils.IngredientInputState
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.components.CustomTextField
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF
import com.velosiped.utility.extensions.ONE
import com.velosiped.ui.R as coreR

@Composable
fun IngredientItemCard(
    ingredientInputState: IngredientInputState,
    ingredientsFound: List<Ingredient>,
    onFocusChanged: (FocusState) -> Unit,
    onNameChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onFatChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
    onMassChange: (String) -> Unit,
    onAutoCompleteInput: (Ingredient) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val readOnly = ingredientInputState.readOnly
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius)))
            .background(CustomTheme.colors.boxCardColors.containerColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(coreR.dimen.space_by_8))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
                modifier = Modifier.fillMaxWidth()
            ) {
                IngredientExpandableTextField(
                    textFieldState = ingredientInputState.nameFieldState,
                    dropMenuItems = ingredientsFound,
                    readOnly = readOnly,
                    onFocusChanged = onFocusChanged,
                    onDropMenuItemClick = { onAutoCompleteInput(it) },
                    onValueChange = { onNameChange(it) },
                    modifier = Modifier
                        .weight(Float.ONE)
                        .padding(dimensionResource(coreR.dimen.space_by_4))
                )
                CustomIcon(
                    painter = painterResource(id = coreR.drawable.delete_from_list),
                    onClick = onDelete,
                    modifier = Modifier.scale(Float.HALF)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(coreR.dimen.space_by_4))
            ) {
                CustomTextField(
                    value = ingredientInputState.proteinFieldState.text,
                    onValueChange = { onProteinChange(it) },
                    underlineHint = stringResource(id = R.string.new_recipe_ingredient_protein),
                    keyboardType = KeyboardType.Number,
                    readOnly = readOnly,
                    alignToStart = true,
                    modifier = Modifier.weight(Float.ONE)
                )
                CustomTextField(
                    value = ingredientInputState.fatFieldState.text,
                    onValueChange = { onFatChange(it) },
                    underlineHint = stringResource(id = R.string.new_recipe_ingredient_fat),
                    keyboardType = KeyboardType.Number,
                    readOnly = readOnly,
                    alignToStart = true,
                    modifier = Modifier.weight(Float.ONE)
                )
                CustomTextField(
                    value = ingredientInputState.carbsFieldState.text,
                    onValueChange = { onCarbsChange(it) },
                    underlineHint = stringResource(id = R.string.new_recipe_ingredient_carbs),
                    keyboardType = KeyboardType.Number,
                    readOnly = readOnly,
                    alignToStart = true,
                    modifier = Modifier.weight(Float.ONE)
                )
                CustomTextField(
                    value = ingredientInputState.massFieldState.text,
                    onValueChange = { onMassChange(it) },
                    underlineHint = stringResource(id = R.string.new_recipe_ingredient_mass),
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(Float.ONE)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        IngredientItemCard(
            ingredientInputState = IngredientInputState(proteinFieldState = TextFieldState(text = "20")),
            ingredientsFound = emptyList(),
            onFocusChanged = {  },
            onNameChange = {  },
            onProteinChange = {  },
            onFatChange = {  },
            onCarbsChange = {  },
            onMassChange = {  },
            onAutoCompleteInput = {  },
            onDelete = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}