package com.velosiped.notes.data.database.exercise

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val type: String
)