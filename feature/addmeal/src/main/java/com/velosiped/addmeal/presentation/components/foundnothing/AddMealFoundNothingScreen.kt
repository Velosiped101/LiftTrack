package com.velosiped.addmeal.presentation.components.foundnothing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.addmeal.R
import com.velosiped.ui.components.ScreenMessage
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

@Composable
fun AddMealFoundNothingScreen(
    modifier: Modifier = Modifier
) {
    ScreenMessage(
        message = stringResource(id = R.string.found_nothing),
        painter = painterResource(id = coreR.drawable.found_nothing),
        modifier = modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme { AddMealFoundNothingScreen() }
}