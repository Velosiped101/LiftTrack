package com.velosiped.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme

@Composable
fun ScreenMessage(
    message: String,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    subtext: String? = null,
    onSubtextClicked: (() -> Unit)? = null
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_12)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            painter?.let {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    tint = CustomTheme.colors.iconsTintColor
                )
            }
            Text(
                text = message,
                style = CustomTheme.typography.screenMessageMedium,
                color = CustomTheme.colors.primaryTextColor
            )
            subtext?.let {
                Text(
                    text = subtext,
                    style = CustomTheme.typography.screenMessageSmall,
                    color = CustomTheme.colors.primaryTextColor,
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onSubtextClicked?.invoke() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageWithSubtext() {
    ScreenMessage(
        message = "Error",
        painter = painterResource(R.drawable.error),
        subtext = "retry",
        onSubtextClicked = {  },
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
private fun MessageWithoutSubtextAndIcon() {
    ScreenMessage(
        message = "Empty",
        modifier = Modifier.fillMaxSize()
    )
}