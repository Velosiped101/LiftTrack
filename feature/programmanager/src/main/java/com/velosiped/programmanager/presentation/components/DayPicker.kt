package com.velosiped.programmanager.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programmanager.R
import com.velosiped.programmanager.presentation.utils.DayOfWeek
import com.velosiped.ui.components.divider.CustomHorizontalDivider
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.ZERO

@Composable
fun DayPicker(
    day: DayOfWeek,
    onDayChange: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = DayOfWeek.entries
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, CustomTheme.colors.selectedOptionColor),
        startY = Float.ZERO,
        endY = integerResource(R.integer.day_picker_gradient_end_y).toFloat()
    )
    var dayTabSize by remember { mutableStateOf(Size.Zero) }
    Column(
        modifier = modifier
            .height(dimensionResource(R.dimen.day_picker_height))
    ) {
        CustomHorizontalDivider()
        Box(modifier = Modifier.fillMaxWidth()){
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    brush = gradient,
                    topLeft = Offset(
                        x = days.indexOf(day) * dayTabSize.width,
                        y = Float.ZERO
                    ),
                    size = dayTabSize
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                days.forEach {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(Float.ONE)
                            .clickable(
                                indication = null,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                }
                            ) { onDayChange(it) }
                            .onGloballyPositioned { coordinates ->
                                dayTabSize = coordinates.size.let {
                                    Size(it.width.toFloat(), it.height.toFloat())
                                }
                            }
                    ) {
                        Text(
                            text = stringResource(id = it.textResId),
                            style = CustomTheme.typography.screenMessageSmall,
                            color = CustomTheme.colors.primaryTextColor
                        )
                    }
                }
            }
        }
        CustomHorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        DayPicker(
            day = DayOfWeek.SATURDAY,
            onDayChange = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}