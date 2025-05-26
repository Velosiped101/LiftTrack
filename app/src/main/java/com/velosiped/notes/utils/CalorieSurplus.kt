package com.velosiped.notes.utils

import com.velosiped.notes.R
import com.velosiped.notes.proto.ProtoCalorieSurplus

enum class CalorieSurplus(val cals: Int, override val textId: Int): TextRepresentable {
    Maintenance(0, R.string.goal_maintenance),
    Surplus(300, R.string.goal_surplus),
    Deficit(-300, R.string.goal_deficit)
}

fun CalorieSurplus.toProto(): ProtoCalorieSurplus {
    return when (this) {
        CalorieSurplus.Maintenance -> ProtoCalorieSurplus.Maintenance
        CalorieSurplus.Surplus -> ProtoCalorieSurplus.Surplus
        CalorieSurplus.Deficit -> ProtoCalorieSurplus.Deficit
    }
}

fun ProtoCalorieSurplus.toCalorieSurplus(): CalorieSurplus {
    return when (this) {
        ProtoCalorieSurplus.Maintenance -> CalorieSurplus.Maintenance
        ProtoCalorieSurplus.Surplus -> CalorieSurplus.Surplus
        ProtoCalorieSurplus.Deficit -> CalorieSurplus.Deficit
        ProtoCalorieSurplus.UNRECOGNIZED -> CalorieSurplus.Maintenance
    }
}