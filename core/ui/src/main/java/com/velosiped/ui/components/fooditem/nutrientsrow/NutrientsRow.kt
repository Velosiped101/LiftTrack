package com.velosiped.ui.components.fooditem.nutrientsrow

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.utils.NutrientType
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.ZERO

@Composable
fun NutrientsRow(
    protein: Float,
    fat: Float,
    carbs: Float,
    modifier: Modifier = Modifier
) {
    val maxValue = maxOf(protein, fat, carbs)

    val proteinWeight = (protein / maxValue).coerceIn(Float.ZERO, Float.ONE)
    val fatWeight = (fat / maxValue).coerceIn(Float.ZERO, Float.ONE)
    val carbsWeight = (carbs / maxValue).coerceIn(Float.ZERO, Float.ONE)

    val proteinWeightIsValid = proteinWeight.isFinite() && proteinWeight != Float.ZERO
    val fatWeightIsValid = fatWeight.isFinite() && fatWeight != Float.ZERO
    val carbsWeightIsValid = carbsWeight.isFinite() && carbsWeight != Float.ZERO

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        if (proteinWeightIsValid) {
            NutrientsRowLineSegment(
                nutrient = NutrientType.PROTEIN,
                value = protein,
                modifier = Modifier.weight(proteinWeight)
            )
        }
        if (fatWeightIsValid) {
            NutrientsRowLineSegment(
                nutrient = NutrientType.FAT,
                value = fat,
                modifier = Modifier.weight(fatWeight)
            )
        }
        if (carbsWeightIsValid) {
            NutrientsRowLineSegment(
                nutrient = NutrientType.CARBS,
                value = carbs,
                modifier = Modifier.weight(carbsWeight)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        NutrientsRow(
            protein = 30f,
            fat = 0f,
            carbs = 66f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}