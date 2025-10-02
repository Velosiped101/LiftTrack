package com.velosiped.settings.presentation.utils.mappers

import com.velosiped.notes.data.datastore.ProtoCalorieSurplus
import com.velosiped.settings.presentation.utils.Goal

fun ProtoCalorieSurplus.toGoal() = when (this) {
    ProtoCalorieSurplus.Maintenance -> Goal.MAINTAIN
    ProtoCalorieSurplus.Surplus -> Goal.GAIN
    ProtoCalorieSurplus.Deficit -> Goal.LOSE
    ProtoCalorieSurplus.UNRECOGNIZED -> Goal.MAINTAIN
}

fun Goal.toProtoCalorieSurplus() = when (this) {
    Goal.MAINTAIN -> ProtoCalorieSurplus.Maintenance
    Goal.GAIN -> ProtoCalorieSurplus.Surplus
    Goal.LOSE -> ProtoCalorieSurplus.Deficit
}