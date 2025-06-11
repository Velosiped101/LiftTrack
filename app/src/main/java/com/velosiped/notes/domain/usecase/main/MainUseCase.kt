package com.velosiped.notes.domain.usecase.main

data class MainUseCase(
    val checkForProgramUpdateUseCase: CheckForProgramUpdateUseCase,
    val cycleGraphDataUseCase: CycleGraphDataUseCase,
    val observeMainScreenInformationUseCase: ObserveMainScreenInformationUseCase,
    val resetProgramProgressUseCase: ResetProgramProgressUseCase
)