package com.velosiped.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.home.R
import com.velosiped.home.presentation.components.diet.CaloriesCircularProgressIndicator
import com.velosiped.home.presentation.components.diet.NutrientBarsColumn
import com.velosiped.ui.components.divider.CustomHorizontalDivider
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.data.NutrientsIntake
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.TWO_THIRDS
import com.velosiped.ui.R as coreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietCard(
    targetCals: Int,
    currentIntake: NutrientsIntake,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicMainCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(coreR.dimen.space_by_4)))
            Text(
                text = stringResource(id = R.string.main_screen_diet_card_header),
                style = CustomTheme.typography.screenMessageMedium,
                color = CustomTheme.colors.primaryTextColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(coreR.dimen.space_by_4))
            )
            CustomHorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
                modifier = Modifier.padding(dimensionResource(coreR.dimen.space_by_4))
            ){
                CaloriesCircularProgressIndicator(
                    currentValue = currentIntake.calories,
                    targetValue = targetCals,
                    modifier = Modifier.weight(Float.TWO_THIRDS)
                )
                NutrientBarsColumn(
                    protein = currentIntake.protein.toInt(),
                    fat = currentIntake.fat.toInt(),
                    carbs = currentIntake.carbs.toInt(),
                    modifier = Modifier.weight(Float.ONE)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        DietCard(
            targetCals = 2500,
            currentIntake = NutrientsIntake(
                protein = 120f,
                fat = 10f,
                carbs = 45f
            ),
            onClick = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}