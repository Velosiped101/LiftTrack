package com.velosiped.notes.presentation.screens.diet.addmeal.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.BaseTopBar
import com.velosiped.notes.presentation.screens.components.CustomIcon
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun AddMealTopBar(
    searchInput: String,
    saveButtonEnabled: Boolean,
    onInputClear: () -> Unit,
    onInputChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseTopBar(
        onNavigateBack = onNavigateBack,
        header = {
            TopSearchBar(
                input = searchInput,
                onInputClear = onInputClear,
                onInputChange = onInputChange
            )
        },
        action = {
            AnimatedVisibility(visible = saveButtonEnabled) {
                IconButton(onClick = onSave) {
                    CustomIcon(painter = painterResource(R.drawable.confirm))
                }
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        AddMealTopBar(
            searchInput = "milk",
            saveButtonEnabled = true,
            onSave = {  },
            onInputClear = {  },
            onInputChange = {  },
            onNavigateBack = {  }
        )
    }
}