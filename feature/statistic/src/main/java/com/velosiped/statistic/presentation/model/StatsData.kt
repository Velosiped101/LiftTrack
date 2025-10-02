package com.velosiped.statistic.presentation.model

import com.velosiped.ui.model.graph.GraphData

data class StatsData(
    val graphData: GraphData? = null,
    val tableData: TableData? = null
)