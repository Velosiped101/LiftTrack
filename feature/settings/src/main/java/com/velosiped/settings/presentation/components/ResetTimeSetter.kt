package com.velosiped.settings.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.settings.R
import com.velosiped.settings.presentation.utils.ResetTime
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

@Composable
fun ResetTimeSetter(
    time: ResetTime,
    onTimeChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val minHour = integerResource(R.integer.reset_hour_min)
    val maxHour = integerResource(R.integer.reset_hour_max)
    val minMinute = integerResource(R.integer.reset_minute_min)
    val maxMinute = integerResource(R.integer.reset_minute_max)

    val hourValues = remember { (minHour..maxHour).toList() }
    val minuteValues = remember { (minMinute..maxMinute).toList() }

    val shape = RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius))
    val borderStroke = BorderStroke(
        width = dimensionResource(coreR.dimen.card_border_width),
        color = CustomTheme.colors.boxCardColors.borderColor
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.settings_reset_time),
            style = CustomTheme.typography.screenMessageSmall,
            color = CustomTheme.colors.primaryTextColor
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(CustomTheme.colors.boxCardColors.containerColor)
                .border(borderStroke, shape)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(coreR.dimen.space_by_4))
            ) {
                WheelPicker(
                    currentValue = time.hour,
                    values = hourValues,
                    valueName = stringResource(id = R.string.settings_hour),
                    onValueChange = { onTimeChange(it, time.minute) }
                )
                WheelPicker(
                    currentValue = time.minute,
                    values = minuteValues,
                    valueName = stringResource(id = R.string.settings_minute),
                    onValueChange = { onTimeChange(time.hour, it) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ResetTimeSetter(
            time = ResetTime(20, 30),
            onTimeChange = { _, _ -> }
        )
    }
}