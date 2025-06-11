package com.velosiped.notes.domain.usecase.diet.foodmanager

import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.domain.repository.DietRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFoodListUseCase @Inject constructor(
    private val dietRepository: DietRepository
) {
    operator fun invoke(): Flow<List<Food>> {
        return dietRepository.getAll()
    }
}