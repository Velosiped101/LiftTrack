package com.velosiped.addmeal.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.addmeal.presentation.utils.SearchMode
import com.velosiped.ui.R
import com.velosiped.ui.components.divider.CustomHorizontalDivider
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE

@Composable
fun SearchModeSwitcher(
    searchMode: SearchMode,
    onModeSelected: (SearchMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        SearchMode.entries.forEach { mode ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(Float.ONE)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onModeSelected(mode) }
            ) {
                Text(
                    text = stringResource(mode.textId),
                    style = CustomTheme.typography.screenMessageSmall,
                    color = CustomTheme.colors.primaryTextColor,
                    modifier = Modifier.padding(dimensionResource(R.dimen.space_by_4))
                )
                if (mode == searchMode) { CustomHorizontalDivider() }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        SearchModeSwitcher(
            searchMode = SearchMode.Local,
            onModeSelected = { }
        )
    }
}