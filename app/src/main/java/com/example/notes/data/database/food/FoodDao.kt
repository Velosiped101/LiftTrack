package com.example.notes.data.database.food

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * from Food")
    fun getFoodList(): Flow<List<Food>>

    @Query("SELECT * from Food WHERE name LIKE '%' || :searchedFood || '%'")
    fun getSearchedFoodList(searchedFood: String): PagingSource<Int,Food>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Food)

    @Delete
    suspend fun deleteFood(listOfIds: List<Food>)
}