package com.velosiped.addmeal.presentation.components.pickedfood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.utils.NutrientType
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme

@Composable
fun NutrientsProgressBarNutrientCounter(
    nutrientType: NutrientType,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4))
    ) {
        Text(
            text = value,
            style = CustomTheme.typography.screenMessageSmall,
            color = CustomTheme.colors.primaryTextColor
        )
        Text(
            text = stringResource(nutrientType.nameTextId),
            style = CustomTheme.typography.underlineHint,
            color = CustomTheme.colors.primaryTextColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        NutrientsProgressBarNutrientCounter(
            nutrientType = NutrientType.FAT,
            value = "45.0 (+10.0)"
        )
    }
}