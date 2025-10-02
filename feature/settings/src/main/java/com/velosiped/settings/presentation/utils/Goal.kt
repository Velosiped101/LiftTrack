package com.velosiped.settings.presentation.utils

import com.velosiped.settings.R

enum class Goal(val cals: Int, val textId: Int) {
    MAINTAIN(0, R.string.goal_maintenance),
    GAIN(300, R.string.goal_surplus),
    LOSE(-300, R.string.goal_deficit)
}