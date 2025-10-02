package com.velosiped.programmanager.presentation.utils

import com.velosiped.utility.extensions.EMPTY
import com.velosiped.utility.extensions.ONE

data class ProgramItemState(
    val id: Long? = null,
    val exercise: String = String.EMPTY,
    val day: DayOfWeek = DayOfWeek.MONDAY,
    val reps: Int = Int.ONE,
    val sets: Int = Int.ONE
)