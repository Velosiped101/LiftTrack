package com.velosiped.home.presentation.utils

import com.velosiped.home.R

enum class TrainingState(val textId: Int) {
    REST(R.string.training_state_rest),
    AWAITS(R.string.training_state_awaits),
    DONE(R.string.training_state_done)
}