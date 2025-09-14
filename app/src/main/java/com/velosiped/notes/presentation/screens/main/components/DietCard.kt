package com.velosiped.notes.presentation.screens.main.components

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
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.CustomHorizontalDivider
import com.velosiped.notes.presentation.screens.main.components.diet.CaloriesCircularProgressIndicator
import com.velosiped.notes.presentation.screens.main.components.diet.NutrientBarsColumn
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.NutrientsIntake
import com.velosiped.notes.utils.ONE
import com.velosiped.notes.utils.TWO_THIRDS

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
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_by_4)))
            Text(
                text = stringResource(id = R.string.diet_card_header),
                style = CustomTheme.typography.screenMessageMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.space_by_4))
            )
            CustomHorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
                modifier = Modifier.padding(dimensionResource(R.dimen.space_by_4))
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

@Preview(showBackground = true)
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