package com.velosiped.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.TWO

@Composable
fun DialogIconButton(
    painter: Painter,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
        modifier = modifier.width(dimensionResource(R.dimen.dialog_icon_button_width))
    ) {
        IconButton(
            onClick = { onClick() }
        ) {
            CustomIcon(painter = painter)
        }
        Text(
            text = text,
            style = CustomTheme.typography.screenMessageSmall,
            maxLines = Int.TWO,
            color = CustomTheme.colors.primaryTextColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        DialogIconButton(
            painter = painterResource(R.drawable.new_recipe),
            text = "New recipe",
            onClick = {}
        )
    }
}