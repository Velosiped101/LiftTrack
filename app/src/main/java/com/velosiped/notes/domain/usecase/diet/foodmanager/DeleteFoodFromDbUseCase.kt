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
    suspend operator fun invoke(foodToDeleteList: List<Food>, context: Context) {
        val imageUris = foodToDeleteList.mapNotNull { it.imageUrl?.toUri() }
        dietRepository.delete(foodToDeleteList)
        imageUris.forEach {
            try {
                context.contentResolver.delete(it, null, null) // i'm tired
            } catch (e: Exception) {
                Log.e("delete image file error", e.message.toString())
            }
        }
    }
}