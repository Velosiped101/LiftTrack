package com.velosiped.notes.domain.repository

import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import kotlinx.coroutines.flow.Flow

interface ProgramRepository {
    fun getAll(): Flow<List<Program>>

    suspend fun insertToProgram(item: Program)

    suspend fun deleteFromProgram(item: Program)

    suspend fun insertToProgramProgress(item: ProgramProgress)

    fun getProgramProgress(): Flow<List<ProgramProgress>>

    fun getLatestWeightDone(exercise: String): Flow<Float>

    fun getWeightIncreaseHint(exercise: String): Flow<Boolean>

    fun getProgramForToday(): Flow<List<Program>>

    suspend fun dropProgramForDay(dayOfWeek: String)

    suspend fun dropProgram()
}