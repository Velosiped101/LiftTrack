package com.velosiped.programexecution.presentation.components.programpage.weightsetter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programexecution.presentation.utils.WeightIncrement
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HUNDRED
import com.velosiped.utility.extensions.PLUS
import com.velosiped.utility.extensions.SPACE
import com.velosiped.ui.R as coreR

@Composable
fun WeightChangeButton(
    prefix: String,
    increment: WeightIncrement,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderStroke = BorderStroke(
        width = dimensionResource(coreR.dimen.card_border_width),
        color = CustomTheme.colors.boxCardColors.borderColor
    )
    val shape = RoundedCornerShape(Int.HUNDRED)
    Box(
        modifier = modifier
            .clip(shape)
            .border(borderStroke, shape)
            .clickable { onClick() }
    ) {
        Text(
            text = prefix + String.SPACE + increment.weight.toString(),
            style = CustomTheme.typography.screenMessageSmall,
            color = CustomTheme.colors.primaryTextColor,
            modifier = Modifier.padding(dimensionResource(coreR.dimen.space_by_4))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        WeightChangeButton(
            increment = WeightIncrement.SMALL,
            prefix = String.PLUS,
            onClick = {  }
        )
    }
}