package com.velosiped.programexecution.presentation

import com.velosiped.training.traininghistory.repository.TrainingHistory

data class ProgramExecUiState(
    val trainingHistory: List<TrainingHistory> = emptyList(),
    val showHintList: List<Boolean> = emptyList(),
    val initialWeight: List<Float> = emptyList()
) {
    val initializationIsComplete: Boolean
        get() = listOf(trainingHistory, showHintList, initialWeight).none { it.isEmpty() }
}