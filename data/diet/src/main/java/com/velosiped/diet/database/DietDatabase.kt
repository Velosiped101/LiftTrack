package com.velosiped.diet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.velosiped.diet.food.databasemodel.FoodDao
import com.velosiped.diet.food.databasemodel.FoodEntity
import com.velosiped.diet.ingredient.databasemodel.IngredientDao
import com.velosiped.diet.ingredient.databasemodel.IngredientEntity
import com.velosiped.diet.mealhistory.databasemodel.MealHistoryDao
import com.velosiped.diet.mealhistory.databasemodel.MealHistoryEntity

@Database(
    entities = [FoodEntity::class, MealHistoryEntity::class, IngredientEntity::class],
    version = 1
)
internal abstract class DietDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun mealHistoryDao(): MealHistoryDao
    abstract fun ingredientDao(): IngredientDao
}