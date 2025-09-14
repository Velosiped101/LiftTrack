package com.velosiped.notes.presentation.screens.components.popupwindow

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.TWO

@Composable
fun PopUpWindowTextHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = CustomTheme.typography.screenMessageMedium,
        maxLines = Int.TWO,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        PopUpWindowTextHeader(
            text = "Pick an option"
        )
    }
}