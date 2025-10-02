package com.velosiped.ui.components.popupwindow

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.TWO

@Composable
fun PopUpWindowTextHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = CustomTheme.typography.screenMessageMedium,
        maxLines = Int.TWO,
        color = CustomTheme.colors.primaryTextColor,
        modifier = modifier
    )
}