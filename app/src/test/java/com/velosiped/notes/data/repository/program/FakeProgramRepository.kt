package com.velosiped.notes.data.repository.program

import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import com.velosiped.notes.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProgramRepository: ProgramRepository {
    private val programProgressList = mutableListOf<ProgramProgress>()
    private val programForToday = mutableListOf<Program>()
    private val program = mutableListOf<Program>()

    override fun getAll(): Flow<List<Program>> {
        return flow { emit(program) }
    }

    override suspend fun insertToProgram(item: Program) {
        program.add(item)
    }

    override suspend fun deleteFromProgram(item: Program) {
        TODO("Not yet implemented")
    }

    override suspend fun insertToProgramProgress(item: ProgramProgress) {
        programProgressList.add(item)
    }

    override fun getProgramProgress(): Flow<List<ProgramProgress>> {
        return flow { emit(programProgressList) }
    }

    fun populateProgramProgress(programProgress: List<ProgramProgress>) {
        programProgress.forEach { programProgressList.add(it) }
    }

    override fun getLatestWeightDone(exercise: String): Flow<Float> {
        TODO("Not yet implemented")
    }

    override fun getWeightIncreaseHint(exercise: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getProgramForToday(): Flow<List<Program>> {
        return flow { emit(programForToday) }
    }

    fun populateProgramForToday(programList: List<Program>) {
        programList.forEach { programForToday.add(it) }
    }

    override suspend fun dropProgramForDay(dayOfWeek: String) {
        program.removeIf { it.dayOfWeek == dayOfWeek }
    }

    override suspend fun dropProgram() {
        program.clear()
    }

}