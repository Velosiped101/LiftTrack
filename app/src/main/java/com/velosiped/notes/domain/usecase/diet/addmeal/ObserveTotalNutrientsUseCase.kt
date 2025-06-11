package com.velosiped.notes.domain.usecase.diet.addmeal

import com.velosiped.notes.data.repository.tempprogress.AppProtoDataStoreRepositoryImpl
import com.velosiped.notes.domain.repository.DietRepository
import com.velosiped.notes.utils.TotalNutrients
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveTotalNutrientsUseCase @Inject constructor(
    private val dietRepository: DietRepository,
    private val protoDataStoreRepository: AppProtoDataStoreRepositoryImpl
) {
    operator fun invoke(): Flow<Pair<Int, TotalNutrients>> = combine(
        protoDataStoreRepository.appProtoStoreFlow,
        dietRepository.getCurrentTotalNutrients()
    ) { dataStore, totalNutrients ->
        val targetCalories = dataStore.appPreferences.targetCalories
        Pair(targetCalories, totalNutrients)
    }
}