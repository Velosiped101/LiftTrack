package com.velosiped.notes.data.repository.diet

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.velosiped.notes.data.api.foodApi.FoodApiService
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.data.database.food.FoodDao
import com.velosiped.notes.data.database.ingredient.Ingredient
import com.velosiped.notes.data.database.ingredient.IngredientDao
import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistoryDao
import com.velosiped.notes.domain.repository.DietRepository
import com.velosiped.notes.utils.SearchMode
import com.velosiped.notes.utils.TotalNutrients
import com.velosiped.notes.utils.cut
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DietRepositoryImpl(
    private val foodDao: FoodDao,
    private val mealHistoryDao: MealHistoryDao,
    private val ingredientDao: IngredientDao,
    private val apiService: FoodApiService
): DietRepository {
    override fun getAll(): Flow<List<Food>> {
        return foodDao.getFoodList()
    }

    override fun getIngredient(name: String): Flow<List<Ingredient>> {
        return ingredientDao.getIngredient(name)
    }

    override suspend fun insertIngredient(ingredient: Ingredient) {
        ingredientDao.insertIngredient(ingredient)
    }

    override suspend fun delete(list: List<Food>) {
        foodDao.deleteFood(list)
    }

    override suspend fun insert(element: Food) {
        foodDao.insertFood(element)
    }

    override suspend fun insertToHistory(element: MealHistory) {
        mealHistoryDao.insert(element)
    }

    override fun getMealHistory(): Flow<List<MealHistory>> {
        return mealHistoryDao.getAll()
    }

    override suspend fun clearMealHistory() {
        mealHistoryDao.clear()
    }

    override fun getCurrentTotalNutrients(): Flow<TotalNutrients> {
        return mealHistoryDao.getAll().map { list ->
            val totalProtein = list.sumOf { it.protein * it.mass/100 }.cut()
            val totalFat = list.sumOf { it.fat * it.mass/100 }.cut()
            val totalCarbs = list.sumOf { it.carbs * it.mass/100 }.cut()
            val totalCals = ((totalProtein + totalCarbs) * 4 + totalFat * 9).toInt()
            TotalNutrients(
                cals = totalCals,
                protein = totalProtein,
                fat = totalFat,
                carbs = totalCarbs
            )
        }
    }

    override fun getFoodPage(
        name: String,
        searchMode: SearchMode
    ): Flow<PagingData<Food>> {
        val pageSize = 10
        return when (searchMode) {
            SearchMode.Local -> getLocalPage(name, pageSize)
            SearchMode.Remote -> getRemotePage(name, pageSize)
        }
    }

    private fun getLocalPage(name: String, pageSize: Int): Flow<PagingData<Food>> {
        val dbPagingSourceFactory = foodDao.getSearchedFoodList(searchedFood = name)
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { dbPagingSourceFactory }
        ).flow
    }

    private fun getRemotePage(name: String, pageSize: Int): Flow<PagingData<Food>> {
        val apiPagingSourceFactory = RemotePagingSource(apiService = apiService, searchTerms = name)
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { apiPagingSourceFactory }
        ).flow
    }
}