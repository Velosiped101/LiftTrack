package com.example.notes.data

import com.example.notes.Service
import com.example.notes.data.local.program.Program
import com.example.notes.data.local.program.ProgramDao
import com.example.notes.data.local.saveddata.program.ProgramProgress
import com.example.notes.data.local.saveddata.program.ProgramProgressDao
import com.example.notes.utils.Date
import kotlinx.coroutines.flow.Flow

class ProgramRepository(
    private val programDao: ProgramDao = Service.db.programDao(),
    private val programProgressDao: ProgramProgressDao = Service.db.programProgressDao()
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

    suspend fun getProgramForToday(): List<Program> {
        return programDao.getProgramForToday(Date.dayOfWeek)
    }

}