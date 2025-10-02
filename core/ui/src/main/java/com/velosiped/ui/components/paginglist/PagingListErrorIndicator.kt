package com.velosiped.ui.components.paginglist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.theme.CustomTheme

@Composable
fun PagingListErrorIndicator(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(dimensionResource(R.dimen.paging_list_indicators_height))
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) { onClick() }
    ) {
        Text(
            text = stringResource(id = R.string.retry),
            style = CustomTheme.typography.screenMessageSmall,
            color = CustomTheme.colors.primaryTextColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        PagingListErrorIndicator(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.space_by_4))
        )
    }
}