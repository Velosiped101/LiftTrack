package com.velosiped.addmeal.presentation.components.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.addmeal.R
import com.velosiped.ui.components.ScreenCircularIndicator
import com.velosiped.ui.theme.CustomTheme

@Composable
fun AddMealLoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        ScreenCircularIndicator(
            modifier = Modifier.size(dimensionResource(R.dimen.add_meal_loading_indicator_size))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme { AddMealLoadingScreen() }
}