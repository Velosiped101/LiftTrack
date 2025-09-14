package com.velosiped.notes.presentation.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun CustomIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        tint = CustomTheme.colors.iconsTintColor,
        modifier = modifier
            .size(dimensionResource(R.dimen.icon_size_small))
            .clickable(
                enabled = onClick != null,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick?.invoke()
            }
    )
}