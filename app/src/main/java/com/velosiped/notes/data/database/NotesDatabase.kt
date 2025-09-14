package com.velosiped.notes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.velosiped.notes.data.database.exercise.Exercise
import com.velosiped.notes.data.database.exercise.ExerciseDao
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.data.database.food.FoodDao
import com.velosiped.notes.data.database.ingredient.Ingredient
import com.velosiped.notes.data.database.ingredient.IngredientDao
import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.data.database.program.ProgramDao
import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistoryDao
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgressDao

@Database(
    entities = [Food::class, Exercise::class, MealHistory::class,
        Program::class, Ingredient::class, ProgramProgress::class],
    version = 1
)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun mealHistoryDao(): MealHistoryDao
    abstract fun programDao(): ProgramDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun programProgressDao(): ProgramProgressDao
}