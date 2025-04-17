package com.example.notes.data.local.saveddata.program

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProgramProgress(
    @PrimaryKey val id: Int? = null,
    val day: Int,
    val month: Int,
    val year: Int,
    val exercise: String,
    val reps: Int
)