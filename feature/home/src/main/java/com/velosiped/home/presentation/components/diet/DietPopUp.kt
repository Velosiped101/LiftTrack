package com.velosiped.home.presentation.components.diet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.home.R
import com.velosiped.ui.components.DialogIconButton
import com.velosiped.ui.components.popupwindow.BasePopUpWindow
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

@Composable
fun DietPopUp(
    onCreateNewRecipe: () -> Unit,
    onAddMeal: () -> Unit,
    onManageLocalFoodDb: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasePopUpWindow(
        options = {
            DialogIconButton(
                painter = painterResource(coreR.drawable.new_recipe),
                text = stringResource(id = R.string.main_screen_dest_new_recipe),
                onClick = { onCreateNewRecipe() }
            )
            DialogIconButton(
                painter = painterResource(coreR.drawable.add_meal),
                text = stringResource(id = R.string.main_screen_dest_add_meal),
                onClick = { onAddMeal() }
            )
            DialogIconButton(
                painter = painterResource(coreR.drawable.manage_food_db),
                text = stringResource(id = R.string.main_screen_dest_food_manager),
                onClick = { onManageLocalFoodDb() }
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        DietPopUp(
            onCreateNewRecipe = {},
            onAddMeal = {},
            onManageLocalFoodDb = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}