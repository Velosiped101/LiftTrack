package com.velosiped.addmeal.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.components.topbar.BaseTopBar
import com.velosiped.ui.theme.CustomTheme

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