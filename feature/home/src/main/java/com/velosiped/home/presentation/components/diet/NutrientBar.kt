package com.velosiped.home.presentation.components.diet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.home.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.utils.NutrientType
import com.velosiped.utility.extensions.NINETY_PERCENT
import com.velosiped.utility.extensions.ONE
import com.velosiped.ui.R as coreR

@Composable
fun NutrientBar(
    nutrient: NutrientType,
    value: Int,
    toMaxValueRatio: Float,
    modifier: Modifier = Modifier
) {
    val color = when (nutrient) {
        NutrientType.PROTEIN -> CustomTheme.colors.nutrientColors.proteinColor
        NutrientType.FAT -> CustomTheme.colors.nutrientColors.fatColor
        NutrientType.CARBS -> CustomTheme.colors.nutrientColors.carbsColor
    }
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        modifier = modifier
    ) {
        Text(
            text = stringResource(nutrient.nameTextId),
            style = CustomTheme.typography.screenMessageSmall,
            color = CustomTheme.colors.primaryTextColor
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
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
                style = CustomTheme.typography.underlineHint,
                maxLines = Int.ONE,
                overflow = TextOverflow.Ellipsis,
                color = CustomTheme.colors.primaryTextColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PartiallyFilledNutrientBar() {
    CustomTheme {
        NutrientBar(
            nutrient = NutrientType.PROTEIN,
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
            nutrient = NutrientType.FAT,
            value = 120,
            toMaxValueRatio = 1f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}