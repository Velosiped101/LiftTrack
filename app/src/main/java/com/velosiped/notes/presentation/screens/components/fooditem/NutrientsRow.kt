package com.velosiped.notes.presentation.screens.components.fooditem

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.DELTA_INCREMENT
import com.velosiped.notes.utils.Nutrient
import com.velosiped.notes.utils.ONE

@Composable
fun NutrientsRow(
    protein: Float,
    fat: Float,
    carbs: Float,
    modifier: Modifier = Modifier
) {
    val maxValue = maxOf(protein, fat, carbs)
    val proteinWeight = (protein / maxValue).coerceIn(Float.DELTA_INCREMENT, Float.ONE)
    val fatWeight = (fat / maxValue).coerceIn(Float.DELTA_INCREMENT, Float.ONE)
    val carbsWeight = (carbs / maxValue).coerceIn(Float.DELTA_INCREMENT, Float.ONE)

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        NutrientsRowSegment(
            nutrient = Nutrient.Protein,
            value = protein,
            modifier = Modifier.weight(proteinWeight)
        )
        NutrientsRowSegment(
            nutrient = Nutrient.Fat,
            value = fat,
            modifier = Modifier.weight(fatWeight)
        )
        NutrientsRowSegment(
            nutrient = Nutrient.Carbs,
            value = carbs,
            modifier = Modifier.weight(carbsWeight)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        NutrientsRow(
            protein = 60f,
            fat = 0.5f,
            carbs = 25f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}