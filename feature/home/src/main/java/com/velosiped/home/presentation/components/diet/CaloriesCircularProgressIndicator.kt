package com.velosiped.home.presentation.components.diet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.home.R
import com.velosiped.ui.components.divider.CustomHorizontalDivider
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.utils.interpolateFourColors
import com.velosiped.utility.extensions.FULL_CIRCLE_ANGLE
import com.velosiped.utility.extensions.HALF_CIRCLE_ANGLE
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.TEN_PERCENT
import com.velosiped.utility.extensions.TWO_QUARTERS_CIRCLE_ANGLE
import com.velosiped.utility.extensions.ZERO
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CaloriesCircularProgressIndicator(
    currentValue: Int,
    targetValue: Int,
    modifier: Modifier = Modifier
) {
    val progress = currentValue/targetValue.toFloat()
    val sweepAngle = (progress * Float.FULL_CIRCLE_ANGLE).coerceIn(Float.ZERO, Float.FULL_CIRCLE_ANGLE)
    val colors = listOf(
        CustomTheme.colors.progressColors.notAchievedColor,
        CustomTheme.colors.progressColors.littleAchievedColor,
        CustomTheme.colors.progressColors.almostAchievedColor,
        CustomTheme.colors.progressColors.achievedColor
    )
    val capColor = interpolateFourColors(progress, colors)
    val strokeWidth = dimensionResource(R.dimen.total_cals_circular_indicator_stroke_width)
    val strokeCapRadius = dimensionResource(R.dimen.total_cals_circular_indicator_cap_diameter)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.aspectRatio(Float.ONE)
    ){
        Canvas(modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.total_cals_circular_indicator_padding))
        ) {
            drawArc(
                color = capColor.copy(alpha = Float.TEN_PERCENT),
                startAngle = Float.ZERO,
                sweepAngle = Float.FULL_CIRCLE_ANGLE,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx())
            )
            rotate(Float.TWO_QUARTERS_CIRCLE_ANGLE) {
                drawArc(
                    brush = Brush.sweepGradient(colors),
                    startAngle = Float.ZERO,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx())
                )
            }
            drawCircle(
                color = capColor,
                radius = strokeCapRadius.toPx(),
                center = Offset(
                    x = center.x * (Int.ONE + sin(sweepAngle * PI.toFloat() / Float.HALF_CIRCLE_ANGLE)),
                    y = center.y * (Int.ONE - cos(sweepAngle * PI.toFloat() / Float.HALF_CIRCLE_ANGLE))
                )
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            Text(
                text = currentValue.toString(),
                style = CustomTheme.typography.screenMessageLarge,
                maxLines = Int.ONE,
                color = CustomTheme.colors.primaryTextColor
            )
            CustomHorizontalDivider()
            Text(
                text = targetValue.toString(),
                style = CustomTheme.typography.screenMessageSmall,
                maxLines = Int.ONE,
                color = CustomTheme.colors.primaryTextColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        CaloriesCircularProgressIndicator(
            currentValue = 1800,
            targetValue = 2700,
            modifier = Modifier.size(200.dp)
        )
    }
}