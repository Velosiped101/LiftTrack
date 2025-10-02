package com.velosiped.diet.food.databasemodel

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FoodDao {
    @Query("SELECT * from FoodEntity")
    fun getFoodList(): Flow<List<FoodEntity>>

    @Query("SELECT * from FoodEntity WHERE name LIKE '%' || :searchedFood || '%'")
    fun getSearchedFoodList(searchedFood: String): PagingSource<Int,FoodEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity)

    @Delete
    suspend fun deleteFood(listOfIds: List<FoodEntity>)
}