package com.velosiped.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme

@Composable
fun CustomSlider(
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    subText: String? = null
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Slider(
            value = value,
            onValueChange = { onValueChange(it) },
            valueRange = range,
            colors = SliderDefaults.colors(
                activeTrackColor = CustomTheme.colors.sliderColors.activeTrackColor,
                inactiveTrackColor = CustomTheme.colors.sliderColors.inactiveTrackColor,
                thumbColor = CustomTheme.colors.sliderColors.thumbColor
            )
        )
        if (subText != null) {
            Text(
                text = subText,
                style = CustomTheme.typography.screenMessageMedium,
                color = CustomTheme.colors.primaryTextColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        CustomSlider(
            subText = "Reps",
            value = 2f,
            range = 1f..10f,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}