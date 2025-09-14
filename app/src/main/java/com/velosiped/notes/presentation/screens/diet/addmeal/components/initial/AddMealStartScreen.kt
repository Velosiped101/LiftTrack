package com.velosiped.notes.presentation.screens.diet.addmeal.components.initial

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.ScreenMessage
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun AddMealStartScreen(
    modifier: Modifier = Modifier
) {
    ScreenMessage(
        message = stringResource(id = R.string.add_meal_start_message),
        modifier = modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        AddMealStartScreen()
    }
}