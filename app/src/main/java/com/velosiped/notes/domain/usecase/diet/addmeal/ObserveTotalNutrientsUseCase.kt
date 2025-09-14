package com.velosiped.notes.domain.usecase.diet.addmeal

import com.velosiped.notes.domain.repository.AppProtoDataStoreRepository
import com.velosiped.notes.domain.repository.DietRepository
import com.velosiped.notes.utils.NutrientsIntake
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveTotalNutrientsUseCase @Inject constructor(
    private val dietRepository: DietRepository,
    private val protoDataStoreRepository: AppProtoDataStoreRepository
) {
    operator fun invoke(): Flow<Pair<Int, NutrientsIntake>> = combine(
        protoDataStoreRepository.appProtoStoreFlow,
        dietRepository.getCurrentTotalNutrients()
    ) { dataStore, intake ->
        val targetCalories = dataStore.appPreferences.targetCalories
        Pair(targetCalories, intake)
    }
}