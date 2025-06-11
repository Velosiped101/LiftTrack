package com.velosiped.notes.data.repository.diet

import androidx.paging.PagingData
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.data.database.ingredient.Ingredient
import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.domain.repository.DietRepository
import com.velosiped.notes.utils.Constants
import com.velosiped.notes.utils.SearchMode
import com.velosiped.notes.utils.TotalNutrients
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDietRepository: DietRepository {

    private val foodList = mutableListOf<Food>()
    private val ingredientList = mutableListOf<Ingredient>()
    private val mealHistory = mutableListOf<MealHistory>()

    override fun getAll(): Flow<List<Food>> {
        return flow { emit(foodList) }
    }

    override fun getIngredient(name: String): Flow<List<Ingredient>> {
        return flow { emit(ingredientList.filter { it.name.contains(name) }) }
    }

    override suspend fun insertIngredient(ingredient: Ingredient) {
        ingredientList.add(ingredient)
    }

    override suspend fun delete(list: List<Food>) {
        foodList.removeIf { list.contains(it) }
    }

    override suspend fun insert(element: Food) {
        foodList.add(element)
    }

    override suspend fun insertToHistory(element: MealHistory) {
        mealHistory.add(element)
    }

    override fun getMealHistory(): Flow<List<MealHistory>> {
        return flow { emit(mealHistory) }
    }

    override fun getFoodPage(name: String, searchMode: SearchMode): Flow<PagingData<Food>> {
        TODO("Not yet implemented")
    }

    override suspend fun clearMealHistory() {
        mealHistory.clear()
    }

    override fun getCurrentTotalNutrients(): Flow<TotalNutrients> {
        val totalProtein = mealHistory.sumOf { it.protein }
        val totalFat = mealHistory.sumOf { it.fat }
        val totalCarbs = mealHistory.sumOf { it.carbs }
        val totalCals = (totalProtein + totalCarbs) * Constants.CAL_PER_GRAM_PROTEIN_CARB + Constants.CAL_PER_GRAM_FAT * totalFat
        val totalNutrients = TotalNutrients(
            protein = totalProtein,
            fat = totalFat,
            carbs = totalCarbs,
            cals = totalCals.toInt()
        )
        return flow { emit(totalNutrients) }
    }

}