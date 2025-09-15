package com.velosiped.notes.presentation.screens.diet.foodManagerScreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.BaseTopBar
import com.velosiped.notes.presentation.screens.components.TopBarHeader
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun FoodManagerTopBar(
    inDeleteMode: Boolean,
    onMainPage: Boolean,
    onNavigateBack: () -> Unit,
    onAdd: () -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseTopBar(
        onNavigateBack = onNavigateBack,
        header = {
            AnimatedVisibility(
                visible = onMainPage,
                enter = slideInHorizontally() + fadeIn(),
                exit = slideOutHorizontally() + fadeOut()
            ) {
                TopBarHeader(text = stringResource(R.string.food_manager_headline))
            }
        },
        action = {
            FoodManagerTopBarActionButton(
                inDeleteMode = inDeleteMode,
                onMainPage = onMainPage,
                onDelete = onDelete,
                onAdd = onAdd,
                onSave = onSave
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        FoodManagerTopBar(
            inDeleteMode = false,
            onMainPage = true,
            onNavigateBack = { },
            onAdd = { },
            onDelete = { },
            onSave = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}