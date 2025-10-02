package com.velosiped.addmeal.presentation.components.pickedfood

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.addmeal.R
import com.velosiped.ui.utils.NutrientType
import com.velosiped.ui.R as coreR
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.utils.interpolateFourColors
import com.velosiped.utility.data.NutrientsIntake
import com.velosiped.utility.extensions.HALF
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.ZERO

@Composable
fun NutrientsProgressBar(
    currentIntake: NutrientsIntake,
    selectedFoodIntake: NutrientsIntake,
    targetCalories: Int,
    modifier: Modifier = Modifier
) {
    val totalProgress = ((currentIntake.calories + selectedFoodIntake.calories) / targetCalories.toFloat()).coerceIn(Float.ZERO, Float.ONE)
    val currentProgress = (currentIntake.calories / targetCalories.toFloat()).coerceIn(Float.ZERO, Float.ONE)
    val additionalProgress = (selectedFoodIntake.calories / targetCalories.toFloat()).coerceIn(Float.ZERO, Float.ONE)
    var totalWidth by remember { mutableFloatStateOf(Float.ZERO) }

    val colorsList = listOf(
        CustomTheme.colors.progressColors.notAchievedColor,
        CustomTheme.colors.progressColors.littleAchievedColor,
        CustomTheme.colors.progressColors.almostAchievedColor,
        CustomTheme.colors.progressColors.achievedColor
    )
    val color = interpolateFourColors(totalProgress, colorsList)

    val sumCals = (currentIntake.calories + selectedFoodIntake.calories).toString()
    val sumProtein = (currentIntake.protein + selectedFoodIntake.protein).toString().format(Int.ONE)
    val sumFat = (currentIntake.fat + selectedFoodIntake.fat).toString().format(Int.ONE)
    val sumCarbs = (currentIntake.carbs + selectedFoodIntake.carbs).toString().format(Int.ONE)

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = sumCals + "/" + "$targetCalories" + "(+${selectedFoodIntake.calories})",
            style = CustomTheme.typography.screenMessageMedium,
            color = CustomTheme.colors.primaryTextColor
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.nutrients_progress_bar_height))
                .onGloballyPositioned { totalWidth = it.size.width.toFloat() }
        ) {
            val currentProgressWidth by animateFloatAsState(
                targetValue = totalWidth * currentProgress
            )
            val additionalProgressWidth by animateFloatAsState(
                targetValue = totalWidth * additionalProgress
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = color,
                    size = Size(width = currentProgressWidth, height = size.height)
                )
                drawRect(
                    color = color.copy(alpha = Float.HALF),
                    topLeft = Offset(x = currentProgressWidth, y = Float.ZERO),
                    size = Size(width = additionalProgressWidth, height = size.height)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            NutrientsProgressBarNutrientCounter(
                nutrientType = NutrientType.PROTEIN,
                value = sumProtein + "(+${selectedFoodIntake.protein})"
            )
            NutrientsProgressBarNutrientCounter(
                nutrientType = NutrientType.FAT,
                value = sumFat + "(+${selectedFoodIntake.fat})"
            )
            NutrientsProgressBarNutrientCounter(
                nutrientType = NutrientType.CARBS,
                value = sumCarbs + "(+${selectedFoodIntake.carbs})"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme{
        NutrientsProgressBar(
            currentIntake = NutrientsIntake(
                protein = 30f,
                fat = 10f,
                carbs = 80f
            ),
            selectedFoodIntake = NutrientsIntake(
                protein = 60f,
                fat = 30f,
                carbs = 45.6f
            ),
            targetCalories = 2500,
            modifier = Modifier.fillMaxWidth()
        )
    }
}