package com.velosiped.notes.presentation.screens.components

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun CustomOutlinedButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonColors(
            containerColor = CustomTheme.colors.outlinedButtonColors.containerColor,
            contentColor = CustomTheme.colors.outlinedButtonColors.contentColor,
            disabledContainerColor = CustomTheme.colors.outlinedButtonColors.disabledContainerColor,
            disabledContentColor = CustomTheme.colors.outlinedButtonColors.disabledContentColor
        ),
        enabled = enabled,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = CustomTheme.typography.screenMessageSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
   CustomTheme { CustomOutlinedButton({}, "Confirm") }
}