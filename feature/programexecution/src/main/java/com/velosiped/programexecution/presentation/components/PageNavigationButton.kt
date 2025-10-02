package com.velosiped.programexecution.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import com.velosiped.ui.R as coreR
import com.velosiped.ui.components.CustomIcon
import com.velosiped.utility.extensions.HALF_CIRCLE_ANGLE
import com.velosiped.utility.extensions.ZERO

@Composable
fun PageNavigationButton(
    isNextButton: Boolean,
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier.clickable {
            if (isVisible) onClick()
        }
    ) {
        CustomIcon(
            painter = painterResource(id = coreR.drawable.next),
            onClick = onClick,
            modifier = Modifier.rotate(if (isNextButton) Float.ZERO else Float.HALF_CIRCLE_ANGLE)
        )
    }
}