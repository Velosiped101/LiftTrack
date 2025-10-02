package com.velosiped.training.program.repository

data class Program(
    val id: Long? = null,
    val exercise: String,
    val day: String,
    val reps: Int
)