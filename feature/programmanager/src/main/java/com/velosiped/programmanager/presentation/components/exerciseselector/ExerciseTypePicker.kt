package com.velosiped.programmanager.presentation.components.exerciseselector

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programmanager.R
import com.velosiped.programmanager.presentation.utils.ExerciseType
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.R as coreR
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.TWO_THIRDS

@Composable
fun ExerciseTypePicker(
    currentType: ExerciseType,
    onExerciseTypeClick: (ExerciseType) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderStroke = BorderStroke(
        dimensionResource(coreR.dimen.card_border_width),
        CustomTheme.colors.boxCardColors.borderColor
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius)))
            .background(CustomTheme.colors.boxCardColors.containerColor)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(dimensionResource(coreR.dimen.space_by_4))
        ) {
            ExerciseType.entries.forEach {
                val selectedTypeModifier =
                    if (it == currentType) Modifier.border(borderStroke, CircleShape)
                    else Modifier
                Box {
                    CustomIcon(
                        painter = painterResource(id = it.iconResId),
                        onClick = { onExerciseTypeClick(it) },
                        size = dimensionResource(R.dimen.exercise_type_icon_size),
                        modifier = selectedTypeModifier.scale(Float.TWO_THIRDS)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        ExerciseTypePicker(
            currentType = ExerciseType.ARMS,
            onExerciseTypeClick = {  }
        )
    }
}