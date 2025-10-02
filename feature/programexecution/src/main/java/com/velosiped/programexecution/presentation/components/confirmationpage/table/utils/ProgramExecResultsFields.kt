package com.velosiped.programexecution.presentation.components.confirmationpage.table.utils

import com.velosiped.programexecution.R

enum class ProgramExecResultsFields(val textResId: Int, val rowWeight: Float) {
    EXERCISE(textResId = R.string.program_exec_results_exercise, rowWeight = 1f),
    WEIGHT(textResId = R.string.program_exec_results_weight, rowWeight = .3f),
    REPS_PLANNED(textResId = R.string.program_exec_results_reps_planned, rowWeight = .3f),
    REPS_DONE(textResId = R.string.program_exec_results_reps_done, rowWeight = .3f)
}