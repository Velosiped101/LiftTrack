package com.velosiped.notes.data.repository.program

import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.data.database.program.ProgramDao
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgressDao
import com.velosiped.notes.utils.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProgramRepository(
    private val programDao: ProgramDao,
    private val programProgressDao: ProgramProgressDao
) {
    fun getAll(): Flow<List<Program>> {
        return programDao.getAll()
    }

    suspend fun insertToProgram(item: Program) {
        programDao.insert(item)
    }

    suspend fun deleteFromProgram(item: Program) {
        programDao.delete(item)
    }

    suspend fun insertToProgramProgress(item: ProgramProgress) {
        programProgressDao.insert(item)
    }

    fun getProgramProgress(): Flow<List<ProgramProgress>> {
        return programProgressDao.getAll()
    }

    fun getLatestWeightDone(exercise: String): Flow<Float> {
        return programProgressDao.getAll().map { progressList ->
            progressList
                .groupBy { it.exercise }
                .filter { it.key == exercise }
                .values
                .lastOrNull()
                ?.groupBy { Triple(it.day, it.month, it.year) }
                ?.values
                ?.lastOrNull()
                ?.minOf { it.weight } ?: 0f
        }
    }

    fun getWeightIncreaseHint(exercise: String): Flow<Boolean> {
        return programProgressDao.getAll().map { progressList ->
            progressList
                .groupBy { it.exercise }
                .filter { it.key == exercise }
                .values
                .lastOrNull()
                ?.groupBy { Triple(it.day, it.month, it.year) }
                ?.values
                ?.lastOrNull()
                ?.all { it.reps >= it.repsPlanned } ?: false
        }
    }

    fun getProgramForToday(): Flow<List<Program>> {
        return programDao.getProgramForToday(Date.dayOfWeek)
    }

    suspend fun dropProgramForDay(dayOfWeek: String) {
        programDao.dropProgramForDay(dayOfWeek)
    }

    suspend fun dropProgram() {
        programDao.dropProgram()
    }

}