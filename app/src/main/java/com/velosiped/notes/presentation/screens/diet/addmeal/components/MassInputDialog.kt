package com.velosiped.notes.presentation.screens.diet.addmeal.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.CustomOutlinedButton
import com.velosiped.notes.presentation.screens.components.CustomTextField
import com.velosiped.notes.presentation.screens.components.popupwindow.BasePopUpWindow
import com.velosiped.notes.presentation.screens.components.popupwindow.PopUpWindowTextHeader
import com.velosiped.notes.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MassInputDialog(
    value: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        BasePopUpWindow(
            options = {
                CustomOutlinedButton(
                    onClick = onConfirm,
                    text = stringResource(R.string.confirm),
                    enabled = value.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            header = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_12)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PopUpWindowTextHeader(text = stringResource(R.string.enter_mass))
                    CustomTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        MassInputDialog(
            value = "200",
            onValueChange = { },
            onConfirm = { },
            onDismiss = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}