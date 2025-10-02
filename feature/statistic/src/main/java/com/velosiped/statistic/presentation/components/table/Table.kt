package com.velosiped.statistic.presentation.components.table

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.statistic.presentation.model.TableData
import com.velosiped.statistic.presentation.model.TableDataValue
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Table(
    data: TableData,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(key1 = data) {
        scrollState.animateScrollToItem(data.values.lastIndex)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_8)),
        modifier = modifier
    ){
        Text(
            text = data.exercise,
            style = CustomTheme.typography.screenMessageLarge,
            color = CustomTheme.colors.primaryTextColor
        )
        TableHeader(modifier = Modifier.fillMaxWidth())
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_8)),
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            val groupedValues = data.values.groupBy { it.date }
            groupedValues.forEach { groupedValue ->
                stickyHeader {
                    Text(
                        text = groupedValue.key,
                        style = CustomTheme.typography.screenMessageMedium,
                        color = CustomTheme.colors.primaryTextColor
                    )
                }
                items(groupedValue.value) {
                    TableItem(
                        repsPlanned = it.repsPlanned,
                        repsDone = it.repsDone,
                        weightDone = it.weight,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp, orientation=landscape")
@Composable
private fun Preview() {
    val data = TableData(
        exercise = "Dead lift",
        values = listOf(
            TableDataValue(date = "1.1.25", repsPlanned = 6, repsDone = 6, weight = 120f),
            TableDataValue(date = "1.1.25", repsPlanned = 6, repsDone = 5, weight = 120f),
            TableDataValue(date = "1.1.25", repsPlanned = 6, repsDone = 4, weight = 120f),
            TableDataValue(date = "3.1.25", repsPlanned = 5, repsDone = 5, weight = 125f),
            TableDataValue(date = "3.1.25", repsPlanned = 5, repsDone = 5, weight = 125f),
            TableDataValue(date = "3.1.25", repsPlanned = 5, repsDone = 3, weight = 130f),
        )
    )
    Table(
        data = data,
        modifier = Modifier.fillMaxSize()
    )
}