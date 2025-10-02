package com.velosiped.training.program.repository

import kotlinx.coroutines.flow.Flow

interface ProgramRepository {

    fun getAllProgramItems(): Flow<List<Program>>

    fun getProgramForToday(): Flow<List<Program>>

    suspend fun insertToProgram(item: Program)

    suspend fun deleteFromProgram(item: Program)

    suspend fun dropProgramForDay(day: String)

    suspend fun dropProgram()

}