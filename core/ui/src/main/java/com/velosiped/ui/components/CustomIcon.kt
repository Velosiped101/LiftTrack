package com.velosiped.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme

@Composable
fun CustomIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp? = null,
    onClick: (() -> Unit)? = null
) {
    val iconModifier = if (onClick != null) {
        modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick() }
    } else modifier

    Icon(
        painter = painter,
        contentDescription = contentDescription,
        tint = CustomTheme.colors.iconsTintColor,
        modifier = iconModifier.size(size ?: dimensionResource(R.dimen.icon_size_default))
    )
}