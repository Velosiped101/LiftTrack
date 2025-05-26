package com.example.notes.utils

import com.example.notes.R

enum class TargetCaloriesSource(override val textId: Int): TextRepresentable {
    UserDefined(R.string.manual),
    AutoCalculated(R.string.auto)
}