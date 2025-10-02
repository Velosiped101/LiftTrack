package com.velosiped.ui.components.divider

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.velosiped.ui.theme.CustomTheme

@Composable
fun CustomHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness
) {
    HorizontalDivider(
        color = CustomTheme.colors.dividerColor,
        thickness = thickness,
        modifier = modifier
    )
}

@Composable
fun CustomVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness
) {
    VerticalDivider(
        color = CustomTheme.colors.dividerColor,
        thickness = thickness,
        modifier = modifier
    )
}