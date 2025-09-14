package com.velosiped.notes.domain.repository

import androidx.paging.PagingData
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.data.database.ingredient.Ingredient
import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.utils.NutrientsIntake
import com.velosiped.notes.utils.SearchMode
import kotlinx.coroutines.flow.Flow

interface DietRepository {
    fun getAll(): Flow<List<Food>>

    fun getIngredient(name: String): Flow<List<Ingredient>>

    suspend fun insertIngredient(ingredient: Ingredient)

    suspend fun delete(list: List<Food>)

    suspend fun insert(element: Food)

    suspend fun insertToHistory(element: MealHistory)

    fun getMealHistory(): Flow<List<MealHistory>>

    fun getFoodPage(name: String, searchMode: SearchMode): Flow<PagingData<Food>>

    suspend fun clearMealHistory()

    fun getCurrentTotalNutrients(): Flow<NutrientsIntake>
}