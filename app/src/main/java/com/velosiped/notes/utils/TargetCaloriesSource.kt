package com.velosiped.notes.utils

import com.velosiped.notes.R

enum class TargetCaloriesSource(override val textId: Int): TextRepresentable {
    UserDefined(R.string.manual),
    AutoCalculated(R.string.auto)
}