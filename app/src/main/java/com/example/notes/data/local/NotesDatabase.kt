package com.example.notes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notes.R
import com.example.notes.data.local.program.Exercise
import com.example.notes.data.local.program.ExerciseDao
import com.example.notes.data.local.food.Food
import com.example.notes.data.local.food.FoodDao
import com.example.notes.data.local.food.Ingredient
import com.example.notes.data.local.food.IngredientDao
import com.example.notes.data.local.program.Program
import com.example.notes.data.local.program.ProgramDao
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.data.local.saveddata.mealhistory.MealHistoryDao
import com.example.notes.data.local.saveddata.program.ProgramProgress
import com.example.notes.data.local.saveddata.program.ProgramProgressDao

@Database(
    entities = [Food::class, Exercise::class, MealHistory::class,
        Program::class, Ingredient::class, ProgramProgress::class],
    version = 8
)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun mealHistoryDao(): MealHistoryDao
    abstract fun programDao(): ProgramDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun programProgressDao(): ProgramProgressDao

    companion object {
        fun createDb(context: Context): NotesDatabase {
            return Room.databaseBuilder(
                context,
                NotesDatabase::class.java,
                "notes-database"
            )
                .createFromAsset("notesAssetDatabase.db")
                .build()
        }
    }
}