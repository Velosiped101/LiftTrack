package com.velosiped.training.program.repositoryimpl

import com.velosiped.training.program.databasemodel.ProgramDao
import com.velosiped.training.program.databasemodel.toProgram
import com.velosiped.training.program.databasemodel.toProgramEntity
import com.velosiped.training.program.repository.Program
import com.velosiped.training.program.repository.ProgramRepository
import com.velosiped.training.program.utils.DayOfWeek
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

internal class ProgramRepositoryImpl(
    private val programDao: ProgramDao
): ProgramRepository {

    override fun getAllProgramItems(): Flow<List<Program>> {
        return programDao.getAll().map { it.map { it.toProgram() } }
    }

    override fun getProgramForToday(): Flow<List<Program>> {
        val calendar: Calendar = Calendar.getInstance()
        val daysMap = mapOf(
            1 to DayOfWeek.SUNDAY.name,
            2 to DayOfWeek.MONDAY.name,
            3 to DayOfWeek.TUESDAY.name,
            4 to DayOfWeek.WEDNESDAY.name,
            5 to DayOfWeek.THURSDAY.name,
            6 to DayOfWeek.FRIDAY.name,
            7 to DayOfWeek.SATURDAY.name
        )
        val currentDayIndex = calendar.get(Calendar.DAY_OF_WEEK)
        val currentDay = daysMap[currentDayIndex] ?: DayOfWeek.MONDAY.name
        return programDao.getProgramForToday(currentDay).map { it.map { it.toProgram() } }
    }

    override suspend fun insertToProgram(item: Program) {
        programDao.insert(item.toProgramEntity())
    }

    override suspend fun deleteFromProgram(item: Program) {
        programDao.delete(item.toProgramEntity())
    }

    override suspend fun dropProgramForDay(dayOfWeek: String) {
        programDao.dropProgramForDay(dayOfWeek)
    }

    override suspend fun dropProgram() {
        programDao.dropProgram()
    }
}