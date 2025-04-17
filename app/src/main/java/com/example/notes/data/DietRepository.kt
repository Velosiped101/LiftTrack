package com.example.notes.data

import com.example.notes.Service
import com.example.notes.data.local.food.Food
import com.example.notes.data.local.food.FoodDao
import com.example.notes.data.local.food.Ingredient
import com.example.notes.data.local.food.IngredientDao
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.data.local.saveddata.mealhistory.MealHistoryDao
import com.example.notes.data.remote.FoodApiConstants
import com.example.notes.data.remote.FoodApiResponse
import com.example.notes.data.remote.FoodApiService
import com.example.notes.data.remote.Product
import com.example.notes.presentation.screens.diet.addMealScreen.FoodHolder
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar

class DietRepository(
    private val dao: FoodDao = Service.db.foodDao(),
    private val mealHistoryDao: MealHistoryDao = Service.db.mealHistoryDao(),
    private val ingredientDao: IngredientDao = Service.db.ingredientDao()
) {
    private val apiService: FoodApiService = Retrofit.Builder()
        .baseUrl(FoodApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FoodApiService::class.java)

    fun getAll(): Flow<List<Food>> {
        return dao.getFoodList()
    }

    suspend fun getIngredient(name: String): List<Ingredient> {
        return ingredientDao.getIngredient(name)
    }

    suspend fun insertIngredient(ingredient: Ingredient) {
        ingredientDao.insertIngredient(ingredient)
    }

    fun getSearched(text: String): List<Food> {
        return dao.getSearchedFoodList(text)
    }

    suspend fun delete(list: List<Food>) {
        dao.deleteFood(list)
    }

    suspend fun update(element: Food) {
        dao.updateFood(element)
    }

    suspend fun insert(element: Food) {
        val formattedFood = element.copy(
            protein = BigDecimal(element.protein).setScale(1, RoundingMode.HALF_UP).toDouble(),
            fat = BigDecimal(element.fat).setScale(1, RoundingMode.HALF_UP).toDouble(),
            carbs = BigDecimal(element.carbs).setScale(1, RoundingMode.HALF_UP).toDouble(),
        )
        dao.insertFood(formattedFood)
    }

    suspend fun insertToHistory(element: MealHistory) {
        mealHistoryDao.insert(element)
    }

    fun getMealHistory(): Flow<List<MealHistory>> {
        return mealHistoryDao.getAll()
    }

    private suspend fun getFoodFromApi(text: String): FoodApiResponse {
        return apiService.getFood(text)
    }

    suspend fun getCombinedFoodList(
        text: String,
        getFromLocal: Boolean,
        getFromRemote: Boolean,
    ): FoodHolder<List<Food>> {
        return try {
            if (getFromLocal && getFromRemote) {
                val localList = getSearched(text)
                val remoteList = getFoodFromApi(text).products.let {
                    formatRemoteList(it)
                }
                val data = mutableListOf<Food>().plus(localList).plus(remoteList)
                FoodHolder.Success(data)
            }
            else if (getFromLocal) {
                val localList = getSearched(text)
                FoodHolder.Success(localList)
            }
            else if (getFromRemote) {
                val remoteList = getFoodFromApi(text).products.let {
                    formatRemoteList(it)
                }
                FoodHolder.Success(remoteList)
            }
            else {
                FoodHolder.Success(emptyList())
            }
        } catch(e: Exception) {
            FoodHolder.Error(e)
        }
    }

    private fun formatRemoteList(list: List<Product>?): List<Food> {
        return list?.mapNotNull {
            if (
                it.productName == null || it.productName == "" ||
                it.nutriments == null ||
                it.nutriments.proteins == null ||
                it.nutriments.fat == null ||
                it.nutriments.carbohydrates == null
            ) null
            else Food(
                name = it.productName,
                protein = it.nutriments.proteins.toDouble(),
                fat = it.nutriments.fat.toDouble(),
                carbs = it.nutriments.carbohydrates.toDouble(),
                imageUrl = it.imageUrl
            )
        } ?: emptyList()
    }

    suspend fun clearMealHistory() {
        mealHistoryDao.clear()
    }

    fun getWorkDelay(): Long {
        val dayDurationInSec = 24*3600
        val dateInstance = Calendar.getInstance()
        val startTime = dateInstance.get(Calendar.HOUR_OF_DAY) * 3600 +
                    dateInstance.get(Calendar.MINUTE) * 60 +
                    dateInstance.get(Calendar.SECOND)
        return (dayDurationInSec - startTime).toLong()
    }
}