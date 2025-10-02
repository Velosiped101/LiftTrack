package com.velosiped.statistic.presentation.components.controlpanel

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.statistic.presentation.utils.GraphDataFormula
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF
import com.velosiped.utility.extensions.HALF_CIRCLE_ANGLE
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.ZERO
import com.velosiped.ui.R as coreR

@Composable
fun FormulaPicker(
    currentFormula: GraphDataFormula,
    onFormulaClick: (GraphDataFormula) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var dropdownMenuWidth by remember { mutableIntStateOf(Int.ZERO) }
    val angle by animateFloatAsState(targetValue = if (isExpanded) Float.HALF_CIRCLE_ANGLE else Float.ZERO)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius)))
            .background(CustomTheme.colors.boxCardColors.containerColor)
            .onGloballyPositioned { dropdownMenuWidth = it.size.width }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(coreR.dimen.space_by_8))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { isExpanded = true }
        ) {
            Text(
                text = stringResource(currentFormula.textId),
                style = CustomTheme.typography.screenMessageSmall.copy(textAlign = TextAlign.Start),
                color = CustomTheme.colors.primaryTextColor,
                modifier = Modifier.weight(Float.ONE)
            )
            CustomIcon(
                painter = painterResource(id = coreR.drawable.expand_meal_history),
                modifier = Modifier
                    .rotate(angle)
                    .scale(Float.HALF)
            )
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.background(CustomTheme.colors.mainBackgroundColor)
                .width(with(LocalDensity.current) { dropdownMenuWidth.toDp() })
        ) {
            GraphDataFormula.entries.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(it.textId),
                            style = CustomTheme.typography.screenMessageSmall.copy(textAlign = TextAlign.Start),
                            color = CustomTheme.colors.primaryTextColor
                        )
                    },
                    onClick = {
                        onFormulaClick(it)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        FormulaPicker(
            currentFormula = GraphDataFormula.VOLUME,
            onFormulaClick = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}