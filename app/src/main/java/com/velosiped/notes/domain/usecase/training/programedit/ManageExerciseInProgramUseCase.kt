package com.velosiped.notes.domain.usecase.training.programedit

import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.domain.repository.ProgramRepository
import javax.inject.Inject

class ManageExerciseInProgramUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {
    suspend fun insert(item: Program, sets: Int) {
        repeat(sets) {
            programRepository.insertToProgram(item)
        }
    }

    suspend fun remove(item: Program) {
        programRepository.deleteFromProgram(item)
    }
}