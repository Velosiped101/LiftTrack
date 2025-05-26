package com.example.notes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notes.data.database.exercise.Exercise
import com.example.notes.data.database.exercise.ExerciseDao
import com.example.notes.data.database.food.Food
import com.example.notes.data.database.food.FoodDao
import com.example.notes.data.database.ingredient.Ingredient
import com.example.notes.data.database.ingredient.IngredientDao
import com.example.notes.data.database.program.Program
import com.example.notes.data.database.program.ProgramDao
import com.example.notes.data.database.saveddata.mealhistory.MealHistory
import com.example.notes.data.database.saveddata.mealhistory.MealHistoryDao
import com.example.notes.data.database.saveddata.programProgress.ProgramProgress
import com.example.notes.data.database.saveddata.programProgress.ProgramProgressDao

@Database(
    entities = [Food::class, Exercise::class, MealHistory::class,
        Program::class, Ingredient::class, ProgramProgress::class],
    version = 11
)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun mealHistoryDao(): MealHistoryDao
    abstract fun programDao(): ProgramDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun programProgressDao(): ProgramProgressDao
}