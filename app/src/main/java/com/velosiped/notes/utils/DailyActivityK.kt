package com.velosiped.notes.utils

import com.velosiped.notes.R
import com.velosiped.notes.proto.ProtoDailyActivityK

enum class DailyActivityK(val k: Float, override val textId: Int): TextRepresentable {
    Minimal(1.2f, R.string.daily_activity_minimal),
    Slight(1.375f, R.string.daily_activity_slight),
    Medium(1.55f, R.string.daily_activity_medium),
    High(1.725f, R.string.daily_activity_high),
    VeryHigh(1.9f, R.string.daily_activity_very_high)
}

fun DailyActivityK.toProto(): ProtoDailyActivityK {
    return when (this) {
        DailyActivityK.Minimal -> ProtoDailyActivityK.Minimal
        DailyActivityK.Slight -> ProtoDailyActivityK.Slight
        DailyActivityK.Medium -> ProtoDailyActivityK.Medium
        DailyActivityK.High -> ProtoDailyActivityK.High
        DailyActivityK.VeryHigh -> ProtoDailyActivityK.VeryHigh
    }
}

fun ProtoDailyActivityK.toDailyActivityK(): DailyActivityK {
    return when (this) {
        ProtoDailyActivityK.Minimal -> DailyActivityK.Minimal
        ProtoDailyActivityK.Slight -> DailyActivityK.Slight
        ProtoDailyActivityK.Medium -> DailyActivityK.Medium
        ProtoDailyActivityK.High -> DailyActivityK.High
        ProtoDailyActivityK.VeryHigh -> DailyActivityK.VeryHigh
        ProtoDailyActivityK.UNRECOGNIZED -> DailyActivityK.Medium
    }
}