package com.velosiped.notes.presentation.screens.main.components.diet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.BasicPopUpWindow
import com.velosiped.notes.presentation.screens.components.DialogIconButton
import com.velosiped.notes.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietDialog(
    onDismiss: () -> Unit,
    onCreateNewRecipe: () -> Unit,
    onAddMeal: () -> Unit,
    onManageLocalFoodDb: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        BasicPopUpWindow(
            options = {
                DialogIconButton(
                    iconId = R.drawable.new_recipe,
                    text = stringResource(id = R.string.new_recipe),
                    onClick = { onCreateNewRecipe() }
                )
                DialogIconButton(
                    iconId = R.drawable.add_meal,
                    text = stringResource(id = R.string.add_meal),
                    onClick = { onAddMeal() }
                )
                DialogIconButton(
                    iconId = R.drawable.manage_food_db,
                    text = stringResource(id = R.string.food_manager),
                    onClick = { onManageLocalFoodDb() }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        DietDialog(
            onDismiss = {},
            onCreateNewRecipe = {},
            onAddMeal = {},
            onManageLocalFoodDb = {}
        )
    }
}