package com.velosiped.newrecipe.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.newrecipe.R
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HUNDRED
import com.velosiped.ui.R as coreR

@Composable
fun NewRecipeFAB(
    onClick: () -> Unit,
    rotationAngle: Float,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(dimensionResource(R.dimen.new_recipe_floating_button_padding_end))
            .clip(RoundedCornerShape(Int.HUNDRED))
            .background(CustomTheme.colors.floatingButtonColor)
    ) {
        CustomIcon(
            painter = painterResource(id = coreR.drawable.back),
            onClick = onClick,
            modifier = Modifier.rotate(rotationAngle)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        NewRecipeFAB(
            onClick = { },
            rotationAngle = 0f
        )
    }
}