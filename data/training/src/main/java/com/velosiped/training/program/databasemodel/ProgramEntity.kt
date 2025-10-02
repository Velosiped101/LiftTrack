package com.velosiped.training.program.databasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class ProgramEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val day: String,
    val exercise: String,
    val reps: Int
)