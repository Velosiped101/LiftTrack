package com.velosiped.notes.domain.usecase.main

import com.velosiped.notes.domain.repository.AppProtoDataStoreRepository
import javax.inject.Inject

class ResetProgramProgressUseCase @Inject constructor(
    private val appProtoDataStoreRepository: AppProtoDataStoreRepository
) {
    suspend operator fun invoke() {
        appProtoDataStoreRepository.resetProgramProgress()
    }
}