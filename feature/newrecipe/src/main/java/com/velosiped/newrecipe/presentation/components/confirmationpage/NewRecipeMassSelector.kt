package com.velosiped.newrecipe.presentation.components.confirmationpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.newrecipe.R
import com.velosiped.ui.components.CustomTextField
import com.velosiped.ui.R as coreR
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.ui.theme.CustomTheme

@Composable
fun NewRecipeMassSelector(
    massFieldState: TextFieldState,
    useAutoCalcMass: Boolean,
    onMassInputChange: (String) -> Unit,
    onAutoMassCalcChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        modifier = modifier
    ) {
        NewRecipeRadioButton(
            selected = useAutoCalcMass,
            text = stringResource(R.string.new_recipe_mass_auto),
            onClick = { onAutoMassCalcChange(true) }
        )
        CustomTextField(
            value = massFieldState.text,
            onValueChange = { onMassInputChange(it) },
            underlineHint = stringResource(R.string.new_recipe_mass),
            readOnly = useAutoCalcMass,
            modifier = Modifier.width(dimensionResource(R.dimen.new_recipe_mass_input_field_width))
        )
        NewRecipeRadioButton(
            selected = !useAutoCalcMass,
            text = stringResource(id = R.string.new_recipe_mass_manual),
            onClick = { onAutoMassCalcChange(false) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        NewRecipeMassSelector(
            massFieldState = TextFieldState(text = "200"),
            useAutoCalcMass = false,
            onMassInputChange = { },
            onAutoMassCalcChange = { }
        )
    }
}