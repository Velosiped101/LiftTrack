package com.velosiped.notes.presentation.screens.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.ui.theme.screenMessageMedium

@Composable
fun SettingsCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicMainCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.screenMessageMedium
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.space_by_12)))
            Icon(
                painter = painterResource(R.drawable.settings),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.size(dimensionResource(R.dimen.settings_card_icon_size))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        SettingsCard(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}