package com.velosiped.addmeal.presentation.components.error

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
fun AddMealErrorScreen(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenMessage(
        message = error,
        painter = painterResource(id = coreR.drawable.error),
        subtext = stringResource(id = R.string.try_again),
        onSubtextClicked = { onRetry() },
        modifier = modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        AddMealErrorScreen(
            error = "Some error",
            onRetry = { }
        )
    }
}