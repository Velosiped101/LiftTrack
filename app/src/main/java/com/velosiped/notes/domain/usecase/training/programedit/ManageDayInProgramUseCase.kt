package com.velosiped.notes.domain.usecase.training.programedit

import com.velosiped.notes.domain.repository.ProgramRepository
import javax.inject.Inject

class ManageDayInProgramUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {
    suspend fun dropProgramForCurrentDay(day: String) {
        programRepository.dropProgramForDay(day)
    }
    suspend fun dropProgram() {
        programRepository.dropProgram()
    }
}