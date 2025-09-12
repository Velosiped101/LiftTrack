package com.velosiped.notes.utils

data class GraphData(
    val exercise: String? = null,
    val values: List<Double> = emptyList(),
    val dates: List<String> = emptyList()
)
