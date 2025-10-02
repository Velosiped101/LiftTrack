package com.velosiped.ui.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.theme.CustomTheme

@Composable
fun CustomOutlinedButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val textColor =
        if (enabled) CustomTheme.colors.outlinedButtonColors.contentColor
        else CustomTheme.colors.outlinedButtonColors.disabledContentColor
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = CustomTheme.colors.outlinedButtonColors.containerColor,
            contentColor = CustomTheme.colors.outlinedButtonColors.contentColor,
            disabledContainerColor = CustomTheme.colors.outlinedButtonColors.disabledContainerColor,
            disabledContentColor = CustomTheme.colors.outlinedButtonColors.disabledContentColor
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = SolidColor(CustomTheme.colors.outlinedButtonColors.borderColor)
        ),
        enabled = enabled,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = CustomTheme.typography.screenMessageSmall,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
   CustomTheme { CustomOutlinedButton({}, "Confirm") }
}