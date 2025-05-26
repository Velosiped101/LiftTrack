package com.example.notes.utils

import com.example.notes.R
import com.example.notes.proto.ProtoSex

enum class Sex(override val textId: Int): TextRepresentable {
    Male(R.string.sex_male),
    Female(R.string.sex_female)
}

fun Sex.toProto(): ProtoSex {
    return when (this) {
        Sex.Male -> ProtoSex.Male
        Sex.Female -> ProtoSex.Female
    }
}

fun ProtoSex.toSex(): Sex {
    return when (this) {
        ProtoSex.Male -> Sex.Male
        ProtoSex.Female -> Sex.Female
        ProtoSex.UNRECOGNIZED -> Sex.Male
    }
}