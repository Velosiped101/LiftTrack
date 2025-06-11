package com.velosiped.notes.data.repository.program

import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.data.database.program.ProgramDao
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgressDao
import com.velosiped.notes.domain.repository.ProgramRepository
import com.velosiped.notes.utils.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProgramRepositoryImpl(
    private val programDao: ProgramDao,
    private val programProgressDao: ProgramProgressDao
): ProgramRepository {
    override fun getAll(): Flow<List<Program>> {
        return programDao.getAll()
    }

    override suspend fun insertToProgram(item: Program) {
        programDao.insert(item)
    }

    override suspend fun deleteFromProgram(item: Program) {
        programDao.delete(item)
    }

    override suspend fun insertToProgramProgress(item: ProgramProgress) {
        programProgressDao.insert(item)
    }

    override fun getProgramProgress(): Flow<List<ProgramProgress>> {
        return programProgressDao.getAll()
    }

    override fun getLatestWeightDone(exercise: String): Flow<Float> {
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

    override fun getWeightIncreaseHint(exercise: String): Flow<Boolean> {
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

    override fun getProgramForToday(): Flow<List<Program>> {
        return programDao.getProgramForToday(Date.dayOfWeek)
    }

    override suspend fun dropProgramForDay(dayOfWeek: String) {
        programDao.dropProgramForDay(dayOfWeek)
    }

    override suspend fun dropProgram() {
        programDao.dropProgram()
    }
}