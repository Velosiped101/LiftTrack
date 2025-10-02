package com.velosiped.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.settings.R
import com.velosiped.settings.presentation.utils.DailyActivity
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SettingsSegmentedButton(
    currentValue: T,
    values: List<T>,
    headerText: String,
    onValueSelected: (T) -> Unit,
    valueContent: @Composable (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = headerText,
            style = CustomTheme.typography.screenMessageSmall,
            color = CustomTheme.colors.primaryTextColor
        )
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            values.forEachIndexed { index, value ->
                SegmentedButton(
                    selected = currentValue == value,
                    onClick = { onValueSelected(value) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = values.size
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeBorderColor = CustomTheme.colors.segmentedButtonColors.activeBorderColor,
                        activeContainerColor = CustomTheme.colors.segmentedButtonColors.activeContainerColor,
                        activeContentColor = CustomTheme.colors.segmentedButtonColors.activeContentColor,
                        inactiveBorderColor = CustomTheme.colors.segmentedButtonColors.inactiveBorderColor,
                        inactiveContainerColor = CustomTheme.colors.segmentedButtonColors.inactiveContainerColor,
                        inactiveContentColor = CustomTheme.colors.segmentedButtonColors.inactiveContentColor
                    )
                ) {
                    valueContent(value)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        SettingsSegmentedButton(
            currentValue = DailyActivity.HIGH,
            values = DailyActivity.entries,
            headerText = stringResource(R.string.daily_activity),
            onValueSelected = { },
            valueContent = { SettingsSegmentedButtonOption(text = stringResource(it.textId)) }
        )
    }
}