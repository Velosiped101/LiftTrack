package com.velosiped.notes.utils

import com.velosiped.notes.R

enum class TrainingState(val textId: Int) {
    REST(R.string.rest_day),
    TRAINING(R.string.training_awaits),
    TRAINING_FINISHED(R.string.training_done)
}