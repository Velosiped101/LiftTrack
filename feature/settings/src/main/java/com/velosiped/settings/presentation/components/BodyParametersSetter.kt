package com.velosiped.settings.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.settings.R
import com.velosiped.settings.presentation.utils.toFloatList
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF
import com.velosiped.ui.R as coreR

@Composable
fun BodyParametersSetter(
    currentBodyMass: Float,
    currentAge: Int,
    currentHeight: Int,
    onBodyMassChange: (Float) -> Unit,
    onAgeChange: (Int) -> Unit,
    onHeightChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val bodyMassMin = integerResource(R.integer.body_mass_min).toFloat()
    val bodyMassMax = integerResource(R.integer.body_mass_max).toFloat()
    val ageMin = integerResource(R.integer.age_min)
    val ageMax = integerResource(R.integer.age_max)
    val heightMin = integerResource(R.integer.height_min)
    val heightMax = integerResource(R.integer.height_max)

    val bodyMassList = remember { (bodyMassMin..bodyMassMax).toFloatList(Float.HALF) }
    val ageValues = remember { (ageMin..ageMax).toList() }
    val heightValues = remember { (heightMin..heightMax).toList() }

    val shape = RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius))
    val borderStroke = BorderStroke(
        width = dimensionResource(coreR.dimen.card_border_width),
        color = CustomTheme.colors.boxCardColors.borderColor
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(CustomTheme.colors.boxCardColors.containerColor)
            .border(borderStroke, shape)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(coreR.dimen.space_by_4))
        ) {
            WheelPicker(
                currentValue = currentAge,
                values = ageValues,
                valueName = stringResource(id = R.string.settings_age),
                onValueChange = onAgeChange
            )
            WheelPicker(
                currentValue = currentHeight,
                values = heightValues,
                valueName = stringResource(id = R.string.settings_height),
                onValueChange = onHeightChange
            )
            WheelPicker(
                currentValue = currentBodyMass,
                values = bodyMassList,
                valueName = stringResource(id = R.string.settings_body_mass),
                onValueChange = onBodyMassChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        BodyParametersSetter(
            currentBodyMass = 72f,
            currentAge = 25,
            currentHeight = 170,
            onBodyMassChange = { },
            onAgeChange = { },
            onHeightChange = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}