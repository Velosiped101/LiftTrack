package com.velosiped.ui.components.popupwindow

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.components.DialogIconButton
import com.velosiped.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasePopUpWindow(
    options: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    header: @Composable (() -> Unit)? = null
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
            header?.invoke()
            options.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    it()
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 5)
@Composable
private fun NoHeaderDialogPreview() {
    CustomTheme {
        BasePopUpWindow(
            options = {
                DialogIconButton(
                    painter = painterResource(R.drawable.statistics),
                    text = "Statistics",
                    onClick = { }
                )
                DialogIconButton(
                    painter = painterResource(R.drawable.manage_program),
                    text = "Manage program",
                    onClick = { }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 5)
@Composable
private fun HeaderDialogPreview() {
    CustomTheme {
        BasePopUpWindow(
            options = {
                DialogIconButton(
                    painter = painterResource(R.drawable.new_session),
                    text = "New session",
                    onClick = { }
                )
                DialogIconButton(
                    painter = painterResource(R.drawable.drop_current),
                    text = "Drop current",
                    onClick = { }
                )
                DialogIconButton(
                    painter = painterResource(R.drawable.eye),
                    text = "Eye",
                    onClick = { }
                )
            },
            header = {
                PopUpWindowTextHeader(
                    text = "Pick one of the following options"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}