package com.velosiped.training.traininghistory.databasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class TrainingHistoryEntity(
    @PrimaryKey val id: Long? = null,
    val day: Int,
    val month: Int,
    val year: Int,
    val exercise: String,
    val reps: Int,
    val repsPlanned: Int,
    val weight: Float
)