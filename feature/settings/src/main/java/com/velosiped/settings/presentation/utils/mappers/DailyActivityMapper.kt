package com.velosiped.settings.presentation.utils.mappers

import com.velosiped.notes.data.datastore.ProtoDailyActivityK
import com.velosiped.settings.presentation.utils.DailyActivity

fun DailyActivity.toProtoDailyActivity(): ProtoDailyActivityK = when (this) {
    DailyActivity.MINIMAL -> ProtoDailyActivityK.Minimal
    DailyActivity.SLIGHT -> ProtoDailyActivityK.Slight
    DailyActivity.MEDIUM -> ProtoDailyActivityK.Medium
    DailyActivity.HIGH -> ProtoDailyActivityK.High
    DailyActivity.VERY_HIGH -> ProtoDailyActivityK.VeryHigh
}

fun ProtoDailyActivityK.toDailyActivity(): DailyActivity = when (this) {
    ProtoDailyActivityK.Minimal -> DailyActivity.MINIMAL
    ProtoDailyActivityK.Slight -> DailyActivity.SLIGHT
    ProtoDailyActivityK.Medium -> DailyActivity.MEDIUM
    ProtoDailyActivityK.High -> DailyActivity.HIGH
    ProtoDailyActivityK.VeryHigh -> DailyActivity.VERY_HIGH
    ProtoDailyActivityK.UNRECOGNIZED -> DailyActivity.MEDIUM
}