package com.velosiped.notes.domain.usecase.main

import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.domain.repository.AppProtoDataStoreRepository
import com.velosiped.notes.domain.repository.DietRepository
import com.velosiped.notes.domain.repository.ProgramRepository
import com.velosiped.notes.domain.usecase.statistics.GetGraphDataUseCase
import com.velosiped.notes.domain.usecase.statistics.GraphDataFormula
import com.velosiped.notes.domain.usecase.statistics.ProgramData
import com.velosiped.notes.proto.AppPreferences
import com.velosiped.notes.utils.TrainingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ObserveMainScreenInformationUseCase @Inject constructor(
    private val dietRepository: DietRepository,
    private val programRepository: ProgramRepository,
    private val appProtoDataStoreRepository: AppProtoDataStoreRepository,
    private val getGraphDataUseCase: GetGraphDataUseCase
) {
    operator fun invoke(): Flow<MainScreenData> {
        return combine(
            dietRepository.getMealHistory(),
            programRepository.getProgramForToday(),
            appProtoDataStoreRepository.appProtoStoreFlow,
            getGraphDataUseCase(GraphDataFormula.Volume)
        ) { mealHistory, program, dataStore, graphData ->
            val programExecLocked = dataStore.programExecLock.programExecutionLocked
            val trainingState = when {
                programExecLocked -> TrainingState.TRAINING_FINISHED
                !programExecLocked && program.isEmpty() -> TrainingState.REST
                else -> TrainingState.TRAINING
            }
            val prefs = dataStore.appPreferences
            MainScreenData(
                mealHistory = mealHistory,
                trainingState = trainingState,
                prefs = prefs,
                graphData = graphData
            )
        }.distinctUntilChanged()
    }
}

data class MainScreenData(
    val mealHistory: List<MealHistory>,
    val trainingState: TrainingState,
    val prefs: AppPreferences,
    val graphData: Map<String, List<ProgramData>>
)