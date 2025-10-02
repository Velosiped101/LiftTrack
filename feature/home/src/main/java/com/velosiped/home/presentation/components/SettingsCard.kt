package com.velosiped.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.home.R
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

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
                text = stringResource(R.string.main_screen_settings_card_header),
                style = CustomTheme.typography.screenMessageMedium,
                color = CustomTheme.colors.primaryTextColor
            )
            Spacer(modifier = Modifier.width(dimensionResource(coreR.dimen.space_by_12)))
            CustomIcon(painter = painterResource(coreR.drawable.settings))
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        SettingsCard(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}