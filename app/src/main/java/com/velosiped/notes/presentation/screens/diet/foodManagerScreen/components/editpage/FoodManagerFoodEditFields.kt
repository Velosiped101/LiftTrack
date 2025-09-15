package com.velosiped.notes.presentation.screens.diet.foodManagerScreen.components.editpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.CustomTextField
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.FoodManagerUiState.FoodInput
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.ONE

@Composable
fun FoodManagerFoodEditFields(
    food: FoodInput,
    onNameChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onFatChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
        modifier = modifier
    ) {
        CustomTextField(
            value = food.name,
            onValueChange = onNameChange,
            underlineHint = stringResource(id = R.string.name),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomTextField(
                value = food.protein,
                onValueChange = onProteinChange,
                underlineHint = stringResource(id = R.string.protein),
                modifier = Modifier.weight(Float.ONE)
            )
            CustomTextField(
                value = food.fat,
                onValueChange = onFatChange,
                underlineHint = stringResource(id = R.string.fat),
                modifier = Modifier.weight(Float.ONE)
            )
            CustomTextField(
                value = food.carbs,
                onValueChange = onCarbsChange,
                underlineHint = stringResource(id = R.string.carbs),
                modifier = Modifier.weight(Float.ONE)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        FoodManagerFoodEditFields(
            food = FoodInput(
                name = "Something",
                protein = "10",
                fat = "45"
            ),
            onNameChange = { },
            onProteinChange = { },
            onFatChange = { },
            onCarbsChange = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}