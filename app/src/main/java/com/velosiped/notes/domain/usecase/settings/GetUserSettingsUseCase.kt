package com.velosiped.notes.domain.usecase.settings

import com.velosiped.notes.domain.repository.AppProtoDataStoreRepository
import com.velosiped.notes.proto.AppProtoStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(
    private val repository: AppProtoDataStoreRepository
) {
    operator fun invoke(): Flow<AppProtoStore> = repository.appProtoStoreFlow
}