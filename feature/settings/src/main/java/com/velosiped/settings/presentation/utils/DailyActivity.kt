package com.velosiped.settings.presentation.utils

import com.velosiped.settings.R

enum class DailyActivity(val k: Float, val textId: Int) {
    MINIMAL(1.2f, R.string.daily_activity_minimal),
    SLIGHT(1.375f, R.string.daily_activity_slight),
    MEDIUM(1.55f, R.string.daily_activity_medium),
    HIGH(1.725f, R.string.daily_activity_high),
    VERY_HIGH(1.9f, R.string.daily_activity_very_high)
}