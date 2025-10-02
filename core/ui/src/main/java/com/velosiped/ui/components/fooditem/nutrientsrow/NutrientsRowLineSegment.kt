package com.velosiped.ui.components.fooditem.nutrientsrow

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.utils.NutrientType

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun NutrientsRowLineSegment(
    nutrient: NutrientType,
    value: Float,
    modifier: Modifier = Modifier
) {
    val color = when (nutrient) {
        NutrientType.PROTEIN -> CustomTheme.colors.nutrientColors.proteinColor
        NutrientType.FAT -> CustomTheme.colors.nutrientColors.fatColor
        NutrientType.CARBS -> CustomTheme.colors.nutrientColors.carbsColor
    }
    val nutrientShortName = when (nutrient) {
        NutrientType.PROTEIN -> stringResource(R.string.protein_short)
        NutrientType.FAT -> stringResource(R.string.fat_short)
        NutrientType.CARBS -> stringResource(R.string.carbs_short)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .height(dimensionResource(R.dimen.nutrients_row_height))
        ) {
            if (minWidth >= dimensionResource(R.dimen.nutrients_row_name_min_width)) {
                Text(
                    text = nutrientShortName,
                    style = CustomTheme.typography.underlineHint,
                    color = CustomTheme.colors.primaryTextColor
                )
            }
        }
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (minWidth >= dimensionResource(R.dimen.nutrients_row_value_min_width)) {
                Text(
                    text = value.toString(),
                    style = CustomTheme.typography.underlineHint,
                    color = CustomTheme.colors.primaryTextColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WideSegment() {
    CustomTheme {
        NutrientsRowLineSegment(
            nutrient = NutrientType.PROTEIN,
            value = 10f,
            modifier = Modifier.width(50.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MiddleSegment() {
    CustomTheme {
        NutrientsRowLineSegment(
            nutrient = NutrientType.FAT,
            value = 10f,
            modifier = Modifier.width(25.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NarrowSegment() {
    CustomTheme {
        NutrientsRowLineSegment(
            nutrient = NutrientType.CARBS,
            value = 10f,
            modifier = Modifier.width(5.dp)
        )
    }
}