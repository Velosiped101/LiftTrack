package com.velosiped.settings.presentation.utils.mappers

import com.velosiped.notes.data.datastore.ProtoResetTime
import com.velosiped.settings.presentation.utils.ResetTime

fun ProtoResetTime.toResetTime() = ResetTime(
    hour = this.hour,
    minute = this.minute
)

fun ResetTime.toProtoResetTime() = ProtoResetTime
    .newBuilder()
    .setHour(this.hour)
    .setMinute(this.minute)
    .build()