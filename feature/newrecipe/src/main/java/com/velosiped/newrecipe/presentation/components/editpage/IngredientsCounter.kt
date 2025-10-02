package com.velosiped.newrecipe.presentation.components.editpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.TWO_THIRDS

@Composable
fun IngredientsCounter(
    value: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_8)),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CustomIcon(
            painter = painterResource(id = R.drawable.minus),
            onClick = onDecrease,
            modifier = Modifier.scale(Float.TWO_THIRDS)
        )
        Text(
            text = value.toString(),
            style = CustomTheme.typography.screenMessageMedium,
            color = CustomTheme.colors.primaryTextColor
        )
        CustomIcon(
            painter = painterResource(id = R.drawable.add_plus),
            onClick = onIncrease,
            modifier = Modifier.scale(Float.TWO_THIRDS)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        IngredientsCounter(
            value = 10,
            onIncrease = { },
            onDecrease = { }
        )
    }
}