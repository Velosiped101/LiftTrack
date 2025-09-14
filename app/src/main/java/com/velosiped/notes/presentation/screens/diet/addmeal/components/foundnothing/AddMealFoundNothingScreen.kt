package com.velosiped.notes.presentation.screens.diet.addmeal.components.foundnothing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.ScreenMessage
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun AddMealFoundNothingScreen(
    modifier: Modifier = Modifier
) {
    ScreenMessage(
        message = stringResource(id = R.string.found_nothing),
        painter = painterResource(id = R.drawable.found_nothing),
        modifier = modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme { AddMealFoundNothingScreen() }
}