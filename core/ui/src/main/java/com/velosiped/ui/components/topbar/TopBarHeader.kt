package com.velosiped.ui.components.topbar

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velosiped.ui.theme.CustomTheme

@Composable
fun TopBarHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = CustomTheme.typography.topBarHeadline,
        color = CustomTheme.colors.primaryTextColor,
        modifier = modifier
    )
}