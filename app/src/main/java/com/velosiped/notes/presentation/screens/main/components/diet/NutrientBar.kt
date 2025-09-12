package com.velosiped.notes.presentation.screens.main.components.diet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.ui.theme.screenMessageSmall
import com.velosiped.notes.ui.theme.underlineHint
import com.velosiped.notes.utils.NINETY_PERCENT
import com.velosiped.notes.utils.Nutrient
import com.velosiped.notes.utils.ONE

@Composable
fun NutrientBar(
    nutrient: Nutrient,
    value: Int,
    toMaxValueRatio: Float,
    modifier: Modifier = Modifier
) {
    val color = when (nutrient) {
        Nutrient.Protein -> CustomTheme.colors.proteinColor
        Nutrient.Fat -> CustomTheme.colors.fatColor
        Nutrient.Carbs -> CustomTheme.colors.carbsColor
    }
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
        modifier = modifier
    ) {
        Text(
            text = stringResource(nutrient.nameTextId),
            style = MaterialTheme.typography.screenMessageSmall
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.nutrient_bar_height))
                    .fillMaxWidth(toMaxValueRatio*Float.NINETY_PERCENT)
                    .background(color)
            )
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.underlineHint,
                maxLines = Int.ONE,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PartiallyFilledNutrientBar() {
    CustomTheme {
        NutrientBar(
            nutrient = Nutrient.Protein,
            value = 40,
            toMaxValueRatio = .3f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilledNutrientBar() {
    CustomTheme {
        NutrientBar(
            nutrient = Nutrient.Fat,
            value = 120,
            toMaxValueRatio = 1f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}