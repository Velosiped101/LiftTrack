package com.velosiped.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.TWENTY_PERCENT

@Composable
fun ScreenCircularIndicator(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        color = CustomTheme.colors.circularProgressIndicatorColor,
        strokeWidth = dimensionResource(R.dimen.circular_indicator_stroke_width),
        strokeCap = StrokeCap.Round,
        modifier = modifier.alpha(Float.TWENTY_PERCENT)
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ScreenCircularIndicator(modifier = Modifier.size(40.dp))
    }
}