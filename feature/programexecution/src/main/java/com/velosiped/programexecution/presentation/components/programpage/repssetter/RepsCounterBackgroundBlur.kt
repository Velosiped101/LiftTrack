package com.velosiped.programexecution.presentation.components.programpage.repssetter

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velosiped.programexecution.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.utils.interpolateFourColors
import com.velosiped.utility.extensions.TEN_PERCENT
import com.velosiped.utility.extensions.TWO
import com.velosiped.utility.extensions.ZERO

@Composable
fun RepsCounterBackgroundBlur(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        CustomTheme.colors.progressColors.notAchievedColor,
        CustomTheme.colors.progressColors.littleAchievedColor,
        CustomTheme.colors.progressColors.almostAchievedColor,
        CustomTheme.colors.progressColors.achievedColor
    )
    val color = interpolateFourColors(
        progress = progress,
        colors = colors
    )
    val gradientColors = listOf(color, color.copy(alpha = Float.TEN_PERCENT))
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(
                    radius = dimensionResource(R.dimen.program_exec_reps_counter_blur_radius),
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
        ) {
            drawRect(
                brush = Brush.horizontalGradient(colors = gradientColors.reversed()),
                topLeft = Offset(x = Float.ZERO, y = Float.ZERO),
                size = Size(width = size.width / Int.TWO, height = size.height)
            )
            drawRect(
                brush = Brush.horizontalGradient(colors = gradientColors),
                topLeft = Offset(x = size.width / Int.TWO, Float.ZERO),
                size = Size(width = size.width / Int.TWO, height = size.height)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        RepsCounterBackgroundBlur(
            progress = .9f,
            modifier = Modifier.fillMaxWidth().height(300.dp)
        )
    }
}