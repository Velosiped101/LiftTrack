package com.velosiped.training.traininghistory.repository

data class TrainingHistory(
    val id: Long? = null,
    val day: Int,
    val month: Int,
    val year: Int,
    val exercise: String,
    val reps: Int,
    val repsPlanned: Int,
    val weight: Float
)