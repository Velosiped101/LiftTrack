package com.velosiped.training.exercise.databasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val type: String
)