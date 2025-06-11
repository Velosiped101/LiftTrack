package com.velosiped.notes.domain.usecase.training.programedit

import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveProgramUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {
    operator fun invoke(): Flow<List<Program>> {
        return programRepository.getAll()
    }
}