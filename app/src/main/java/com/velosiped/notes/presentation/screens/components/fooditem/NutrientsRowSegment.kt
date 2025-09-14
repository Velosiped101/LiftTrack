package com.velosiped.notes.presentation.screens.components.fooditem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.Nutrient
import com.velosiped.notes.utils.ONE

@Composable
fun NutrientsRowSegment(
    nutrient: Nutrient,
    value: Float,
    modifier: Modifier = Modifier
) {
    val color = when (nutrient) {
        Nutrient.Protein -> CustomTheme.colors.nutrientColors.proteinColor
        Nutrient.Fat -> CustomTheme.colors.nutrientColors.fatColor
        Nutrient.Carbs -> CustomTheme.colors.nutrientColors.carbsColor
    }
    val nutrientShortName = when (nutrient) {
        Nutrient.Protein -> stringResource(R.string.protein_short)
        Nutrient.Fat -> stringResource(R.string.fat_short)
        Nutrient.Carbs -> stringResource(R.string.carbs_short)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .height(dimensionResource(R.dimen.nutrients_row_height))
        ) {
            Text(
                text = nutrientShortName,
                style = CustomTheme.typography.underlineHint
            )
        }
        Text(
            text = value.toString(),
            style = CustomTheme.typography.screenMessageSmall,
            maxLines = Int.ONE,
            overflow = TextOverflow.Visible
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WideSegment() {
    CustomTheme {
        NutrientsRowSegment(
            nutrient = Nutrient.Protein,
            value = 20f,
            modifier = Modifier.width(50.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MiddleSegment() {
    CustomTheme {
        NutrientsRowSegment(
            nutrient = Nutrient.Protein,
            value = 20f,
            modifier = Modifier.width(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NarrowSegment() {
    CustomTheme {
        NutrientsRowSegment(
            nutrient = Nutrient.Protein,
            value = 20f,
            modifier = Modifier.width(5.dp)
        )
    }
}