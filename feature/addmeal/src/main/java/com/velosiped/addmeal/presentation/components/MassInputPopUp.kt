package com.velosiped.addmeal.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.addmeal.R
import com.velosiped.ui.components.CustomOutlinedButton
import com.velosiped.ui.components.CustomTextField
import com.velosiped.ui.components.popupwindow.BasePopUpWindow
import com.velosiped.ui.components.popupwindow.PopUpWindowTextHeader
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

@Composable
fun MassInputPopUp(
    inputState: TextFieldState,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasePopUpWindow(
        options = {
            CustomOutlinedButton(
                onClick = onConfirm,
                text = stringResource(R.string.add_meal_confirm),
                enabled = inputState.text.isNotBlank(),
                modifier = modifier
            )
        },
        header = {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_12)),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                PopUpWindowTextHeader(text = stringResource(R.string.enter_mass_in_grams))
                CustomTextField(
                    value = inputState.text,
                    onValueChange = onValueChange,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        MassInputPopUp(
            inputState = TextFieldState(text = "200"),
            onValueChange = { },
            onConfirm = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}