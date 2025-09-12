package com.velosiped.notes.presentation.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.ui.theme.screenMessageSmall
import com.velosiped.notes.utils.TWO

@Composable
fun DialogIconButton(
    iconId: Int,
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
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.size(dimensionResource(R.dimen.dialog_icon_size))
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.screenMessageSmall,
            maxLines = Int.TWO
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        DialogIconButton(
            iconId = R.drawable.new_recipe,
            text = "New recipe",
            onClick = {}
        )
    }
}