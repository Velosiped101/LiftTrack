package com.velosiped.foodmanager.presentation.components.editpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.foodmanager.R
import com.velosiped.foodmanager.presentation.utils.FoodInputState
import com.velosiped.ui.R as coreR
import com.velosiped.ui.components.CustomTextField
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE

@Composable
fun FoodManagerFoodEditFields(
    foodInputState: FoodInputState,
    onNameChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onFatChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        modifier = modifier
    ) {
        CustomTextField(
            value = foodInputState.nameFieldState.text,
            onValueChange = onNameChange,
            underlineHint = stringResource(id = R.string.food_manager_name),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomTextField(
                value = foodInputState.proteinFieldState.text,
                onValueChange = onProteinChange,
                keyboardType = KeyboardType.Decimal,
                underlineHint = stringResource(id = R.string.food_manager_protein),
                modifier = Modifier.weight(Float.ONE)
            )
            CustomTextField(
                value = foodInputState.fatFieldState.text,
                onValueChange = onFatChange,
                keyboardType = KeyboardType.Decimal,
                underlineHint = stringResource(id = R.string.food_manager_fat),
                modifier = Modifier.weight(Float.ONE)
            )
            CustomTextField(
                value = foodInputState.carbsFieldState.text,
                onValueChange = onCarbsChange,
                keyboardType = KeyboardType.Decimal,
                underlineHint = stringResource(id = R.string.food_manager_carbs),
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
            foodInputState = FoodInputState(),
            onNameChange = { },
            onProteinChange = { },
            onFatChange = { },
            onCarbsChange = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}