package com.example.notes.data.database.program

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Program(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val dayOfWeek: String,
    val exercise: String,
    val reps: Int
)