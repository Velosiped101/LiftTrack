package com.velosiped.notes.domain.usecase.training.programexec

import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import com.velosiped.notes.domain.repository.AppProtoDataStoreRepository
import com.velosiped.notes.utils.toProgramTempProgressItemsList
import javax.inject.Inject

class UpdateStoredProgressUseCase @Inject constructor(
    private val appProtoDataStoreRepository: AppProtoDataStoreRepository
) {
    suspend operator fun invoke(programProgress: List<ProgramProgress>) {
        appProtoDataStoreRepository.saveTempProgramProgress(
            programProgress.toProgramTempProgressItemsList()
        )
    }
}