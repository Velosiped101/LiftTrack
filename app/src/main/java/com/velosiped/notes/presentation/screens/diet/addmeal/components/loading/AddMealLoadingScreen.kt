package com.velosiped.notes.presentation.screens.diet.addmeal.components.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.TWENTY_PERCENT

@Composable
fun AddMealLoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = CustomTheme.colors.circularProgressIndicatorColor,
            strokeWidth = dimensionResource(R.dimen.add_meal_loading_indicator_stroke_width),
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .align(Alignment.Center)
                .size(dimensionResource(R.dimen.add_meal_loading_indicator_size))
                .alpha(Float.TWENTY_PERCENT)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme { AddMealLoadingScreen() }
}