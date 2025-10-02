package com.velosiped.home.presentation.components.diet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.utils.NutrientType

@Composable
fun NutrientBarsColumn(
    protein: Int,
    fat: Int,
    carbs: Int,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_8)),
        modifier = modifier
    ) {
        val maxNutrient = maxOf(protein, fat, carbs).toFloat()
        NutrientBar(
            nutrient = NutrientType.PROTEIN,
            value = protein,
            toMaxValueRatio = protein / maxNutrient
        )
        NutrientBar(
            nutrient = NutrientType.FAT,
            value = fat,
            toMaxValueRatio = fat / maxNutrient
        )
        NutrientBar(
            nutrient = NutrientType.CARBS,
            value = carbs,
            toMaxValueRatio = carbs / maxNutrient
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        NutrientBarsColumn(
            protein = 100,
            fat = 70,
            carbs = 210,
            modifier = Modifier.fillMaxWidth()
        )
    }
}