package com.example.notes.data.repository.diet

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.notes.data.api.foodApi.FoodApiResponse
import com.example.notes.data.api.foodApi.FoodApiService
import com.example.notes.data.api.foodApi.cut
import com.example.notes.data.database.food.Food
import com.example.notes.data.database.food.FoodDao
import com.example.notes.data.database.ingredient.Ingredient
import com.example.notes.data.database.ingredient.IngredientDao
import com.example.notes.data.database.saveddata.mealhistory.MealHistory
import com.example.notes.data.database.saveddata.mealhistory.MealHistoryDao
import com.example.notes.utils.SearchMode
import com.example.notes.utils.TotalNutrients
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DietRepository(
    private val foodDao: FoodDao,
    private val mealHistoryDao: MealHistoryDao,
    private val ingredientDao: IngredientDao,
    private val apiService: FoodApiService
) {
    fun getAll(): Flow<List<Food>> {
        return foodDao.getFoodList()
    }

    fun getIngredient(name: String): Flow<List<Ingredient>> {
        return ingredientDao.getIngredient(name)
    }

    suspend fun insertIngredient(ingredient: Ingredient) {
        ingredientDao.insertIngredient(ingredient)
    }

    suspend fun delete(list: List<Food>) {
        foodDao.deleteFood(list)
    }

    suspend fun insert(element: Food) {
        foodDao.insertFood(element)
    }

    suspend fun insertToHistory(element: MealHistory) {
        mealHistoryDao.insert(element)
    }

    fun getMealHistory(): Flow<List<MealHistory>> {
        return mealHistoryDao.getAll()
    }

    private suspend fun getFoodFromApi(text: String, page: Int): FoodApiResponse {
        return apiService.getFood(text, page)
    }

    fun getFoodPage(
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

    suspend fun clearMealHistory() {
        mealHistoryDao.clear()
    }

    fun getCurrentTotalNutrients(): Flow<TotalNutrients> {
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
}