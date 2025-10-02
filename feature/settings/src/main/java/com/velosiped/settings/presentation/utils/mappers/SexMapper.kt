package com.velosiped.settings.presentation.utils.mappers

import com.velosiped.notes.data.datastore.ProtoSex
import com.velosiped.settings.presentation.utils.Sex

fun Sex.toProtoSex(): ProtoSex = when (this) {
    Sex.MALE -> ProtoSex.Male
    Sex.FEMALE -> ProtoSex.Female
}

fun ProtoSex.toSex(): Sex = when (this) {
    ProtoSex.Male -> Sex.MALE
    ProtoSex.Female -> Sex.FEMALE
    ProtoSex.UNRECOGNIZED -> Sex.MALE
}