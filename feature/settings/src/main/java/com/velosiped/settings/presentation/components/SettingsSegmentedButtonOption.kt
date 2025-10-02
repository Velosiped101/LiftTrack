package com.velosiped.settings.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE

@Composable
fun SettingsSegmentedButtonOption(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        maxLines = Int.ONE,
        overflow = TextOverflow.Clip,
        style = CustomTheme.typography.screenMessageSmall,
        color = CustomTheme.colors.primaryTextColor,
        modifier = modifier
    )
}