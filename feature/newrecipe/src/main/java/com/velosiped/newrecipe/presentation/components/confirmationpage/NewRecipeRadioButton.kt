package com.velosiped.newrecipe.presentation.components.confirmationpage

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.theme.CustomTheme

@Composable
fun NewRecipeRadioButton(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = CustomTheme.colors.radioButtonColors.selectedColor,
                unselectedColor = CustomTheme.colors.radioButtonColors.unselectedColor
            )
        )
        Text(
            text = text,
            style = CustomTheme.typography.underlineHint
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        NewRecipeRadioButton(
            selected = true,
            text = "Auto",
            onClick = { }
        )
    }
}