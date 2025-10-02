package com.velosiped.statistic.presentation.components.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.statistic.presentation.components.table.utils.TableFields
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.ui.R as coreR

@Composable
fun TableHeader(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.box_card_corner_radius)))
            .background(CustomTheme.colors.boxCardColors.containerColor)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(coreR.dimen.space_by_4))
        ) {
            TableFields.entries.forEach {
                Text(
                    text = stringResource(it.textResId),
                    style = CustomTheme.typography.screenMessageMedium,
                    maxLines = Int.ONE,
                    overflow = TextOverflow.Ellipsis,
                    color = CustomTheme.colors.primaryTextColor,
                    modifier = Modifier.weight(Float.ONE)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        TableHeader(modifier = Modifier.fillMaxWidth())
    }
}