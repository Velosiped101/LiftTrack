package com.velosiped.statistic.presentation.components.controlpanel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.statistic.R
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF
import com.velosiped.ui.R as coreR

@Composable
fun ControlPanelHeader(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius)))
            .background(CustomTheme.colors.boxCardColors.containerColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(coreR.dimen.space_by_8))
        ) {
            Text(
                text = stringResource(id = R.string.stats_return),
                style = CustomTheme.typography.screenMessageSmall,
                color = CustomTheme.colors.primaryTextColor
            )
            CustomIcon(
                painter = painterResource(id = coreR.drawable.back),
                onClick = onNavigateBack,
                modifier = Modifier.scale(Float.HALF)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme { ControlPanelHeader({}) }
}