package com.velosiped.settings.presentation.components

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.settings.R
import com.velosiped.settings.presentation.utils.getClosestIndex
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

@Composable
fun <T: Number> WheelPicker(
    currentValue: T,
    values: List<T>,
    valueName: String,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val wheelPickerHeight = dimensionResource(R.dimen.wheel_picker_height)
    val wheelPickerWidth = dimensionResource(R.dimen.wheel_picker_width)
    val listState = rememberLazyListState()
    val currentIndex = values.getClosestIndex(currentValue)
    LaunchedEffect(key1 = currentIndex) {
        listState.animateScrollToItem(currentIndex)
    }
    LaunchedEffect(key1 = listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val fixedIndex = listState.firstVisibleItemIndex
            values.getOrNull(fixedIndex)?.let {
                onValueChange(it)
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomIcon(painter = painterResource(id = coreR.drawable.two_way_arrow))
            LazyColumn(
                state = listState,
                flingBehavior = rememberSnapFlingBehavior(listState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(wheelPickerHeight)
            ) {
                items(values) { value ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(wheelPickerWidth, wheelPickerHeight)
                    ) {
                        Text(
                            text = value.toString(),
                            style = CustomTheme.typography.screenMessageLarge,
                            color = CustomTheme.colors.primaryTextColor
                        )
                    }
                }
            }
        }
        Text(
            text = valueName,
            style = CustomTheme.typography.screenMessageSmall,
            color = CustomTheme.colors.primaryTextColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        WheelPicker(
            currentValue = 25f,
            values = listOf(0f, 5f, 10f, 15f, 20f, 25f, 30f, 35f),
            valueName = "Age",
            onValueChange = { }
        )
    }
}