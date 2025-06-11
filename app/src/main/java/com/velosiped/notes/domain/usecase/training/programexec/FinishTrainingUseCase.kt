package com.velosiped.notes.domain.usecase.training.programexec

import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import com.velosiped.notes.domain.repository.AppProtoDataStoreRepository
import com.velosiped.notes.domain.repository.ProgramRepository
import javax.inject.Inject

class FinishTrainingUseCase @Inject constructor(
    private val appProtoDataStoreRepository: AppProtoDataStoreRepository,
    private val programRepository: ProgramRepository
) {
    suspend operator fun invoke(programProgress: List<ProgramProgress>) {
        programProgress.forEach {
            programRepository.insertToProgramProgress(it)
        }
        appProtoDataStoreRepository.finishProgramExecution()
    }
}