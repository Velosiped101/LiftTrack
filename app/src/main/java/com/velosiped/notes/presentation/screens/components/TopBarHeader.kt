package com.velosiped.notes.presentation.screens.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun TopBarHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = CustomTheme.typography.topBarHeadline,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme { TopBarHeader("Settings") }
}