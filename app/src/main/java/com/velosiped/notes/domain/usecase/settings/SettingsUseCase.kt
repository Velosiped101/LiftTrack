package com.velosiped.notes.domain.usecase.settings

data class SettingsUseCase(
    val getUserSettingsUseCase: GetUserSettingsUseCase,
    val updateUserSettingsUseCase: UpdateUserSettingsUseCase
)
