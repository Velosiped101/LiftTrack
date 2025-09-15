package com.velosiped.notes.domain.usecase.diet.foodmanager

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.domain.repository.DietRepository
import javax.inject.Inject

class DeleteFoodFromDbUseCase @Inject constructor(
    private val dietRepository: DietRepository
) {
    suspend operator fun invoke(foodToDeleteList: List<Food>) {
        dietRepository.delete(foodToDeleteList)
    }
}