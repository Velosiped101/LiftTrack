package com.velosiped.diet.food.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.velosiped.diet.food.databasemodel.FoodDao
import com.velosiped.diet.food.databasemodel.toFood
import com.velosiped.diet.food.databasemodel.toFoodEntity
import com.velosiped.diet.food.datasource.api.foodApi.FoodApiService
import com.velosiped.diet.food.datasource.pagingsource.RemotePagingSource
import com.velosiped.diet.food.repository.Food
import com.velosiped.diet.food.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class FoodRepositoryImpl(
    private val foodDao: FoodDao,
    private val apiService: FoodApiService
): FoodRepository {

    override fun getAllDatabaseItems(): Flow<List<Food>> {
        return foodDao.getFoodList().map { it.map { it.toFood() } }
    }

    override suspend fun deleteFromDatabase(list: List<Food>) {
        foodDao.deleteFood(list.map { it.toFoodEntity() })
    }

    override suspend fun insertToDatabase(element: Food) {
        foodDao.insertFood(element.toFoodEntity())
    }

    override fun getFoodApiPage(
        name: String,
        searchInLocal: Boolean
    ): Flow<PagingData<Food>> {
        return if (searchInLocal) {
            getLocalPage(name)
        } else {
            getRemotePage(name)
        }
    }

    private fun getLocalPage(name: String): Flow<PagingData<Food>> {
        val dbPagingSourceFactory = foodDao.getSearchedFoodList(searchedFood = name)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { dbPagingSourceFactory }
        ).flow.map { it.map { it.toFood() } }
    }

    private fun getRemotePage(name: String): Flow<PagingData<Food>> {
        val apiPagingSourceFactory = RemotePagingSource(apiService = apiService, searchTerms = name)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { apiPagingSourceFactory }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}