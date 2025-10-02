package com.velosiped.ui.components.paginglist

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.ZERO

@Composable
fun PagingListLoadingIndicator(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = Float.ZERO,
        targetValue = Float.ONE,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(integerResource(R.integer.paging_list_loading_indicator_tween_duration)),
            repeatMode = RepeatMode.Reverse
        )
    )
    val color = CustomTheme.colors.pagingIndicatorColor
    val shape = RoundedCornerShape(
        topStart = dimensionResource(R.dimen.box_card_corner_radius),
        topEnd = dimensionResource(R.dimen.box_card_corner_radius)
    )
    Box(
        modifier = modifier
            .height(dimensionResource(R.dimen.paging_list_indicators_height))
            .clip(shape)
            .alpha(alpha)
            .background(color)
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        PagingListLoadingIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }
}