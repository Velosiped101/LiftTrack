package com.velosiped.foodmanager.presentation.components

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
import com.velosiped.foodmanager.R
import com.velosiped.ui.components.topbar.BaseTopBar
import com.velosiped.ui.components.topbar.TopBarHeader
import com.velosiped.ui.theme.CustomTheme

@Composable
fun FoodManagerTopBar(
    inDeleteMode: Boolean,
    onMainPage: Boolean,
    confirmButtonIsVisible: Boolean,
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
                atMainPage = onMainPage,
                confirmButtonIsVisible = confirmButtonIsVisible,
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
            confirmButtonIsVisible = false,
            onNavigateBack = { },
            onAdd = { },
            onDelete = { },
            onSave = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}