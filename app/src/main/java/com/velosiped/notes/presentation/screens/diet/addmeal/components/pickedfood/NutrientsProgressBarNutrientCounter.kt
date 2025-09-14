package com.velosiped.notes.presentation.screens.diet.addmeal.components.pickedfood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.Nutrient

@Composable
fun NutrientsProgressBarNutrientCounter(
    nutrient: Nutrient,
    value: String
) {
    val nutrientName = when (nutrient) {
        Nutrient.Protein -> stringResource(id = R.string.protein)
        Nutrient.Fat -> stringResource(id = R.string.fat)
        Nutrient.Carbs -> stringResource(id = R.string.carbs)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4))
    ) {
        Text(
            text = value,
            style = CustomTheme.typography.screenMessageSmall
        )
        Text(
            text = nutrientName,
            style = CustomTheme.typography.underlineHint
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        NutrientsProgressBarNutrientCounter(
            nutrient = Nutrient.Fat,
            value = "45.0 (+10.0)"
        )
    }
}