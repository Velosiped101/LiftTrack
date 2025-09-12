package com.velosiped.notes.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.TWO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicPopUpWindow(
    options: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    headerText: String? = null
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.popup_window_corner_radius)))
            .background(CustomTheme.colors.popUpWindowBackgroundColor)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_8)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.space_by_8))
        ) {
            headerText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                    maxLines = Int.TWO
                )
            }
            options.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    it()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoHeaderDialogPreview() {
    CustomTheme {
        BasicPopUpWindow(
            options = {
                DialogIconButton(
                    iconId = R.drawable.statistics,
                    text = "Statistics",
                    onClick = { }
                )
                DialogIconButton(
                    iconId = R.drawable.manage_program,
                    text = "Manage program",
                    onClick = { }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderDialogPreview() {
    CustomTheme {
        BasicPopUpWindow(
            options = {
                DialogIconButton(
                    iconId = R.drawable.new_session,
                    text = "New session",
                    onClick = { }
                )
                DialogIconButton(
                    iconId = R.drawable.drop_current,
                    text = "Drop current",
                    onClick = { }
                )
                DialogIconButton(
                    iconId = R.drawable.eye,
                    text = "Eye",
                    onClick = { }
                )
            },
            headerText = "Pick one of the following options",
            modifier = Modifier.fillMaxWidth()
        )
    }
}