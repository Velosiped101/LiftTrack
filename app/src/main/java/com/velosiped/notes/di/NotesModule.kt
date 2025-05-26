package com.velosiped.notes.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import androidx.work.WorkManager
import com.velosiped.notes.data.api.feedbackApi.FeedbackApiConstants
import com.velosiped.notes.data.api.feedbackApi.FeedbackApiService
import com.velosiped.notes.data.api.foodApi.FoodApiConstants
import com.velosiped.notes.data.api.foodApi.FoodApiService
import com.velosiped.notes.data.database.NotesDatabase
import com.velosiped.notes.data.database.exercise.ExerciseDao
import com.velosiped.notes.data.database.food.FoodDao
import com.velosiped.notes.data.database.ingredient.IngredientDao
import com.velosiped.notes.data.database.program.ProgramDao
import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistoryDao
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgressDao
import com.velosiped.notes.data.repository.diet.DietRepository
import com.velosiped.notes.data.repository.exercise.ExerciseRepository
import com.velosiped.notes.data.repository.program.ProgramRepository
import com.velosiped.notes.data.repository.tempProgress.AppProtoDataStoreRepository
import com.velosiped.notes.data.repository.tempProgress.appProtoDataStore
import com.velosiped.notes.proto.AppProtoStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesModule {

    @Provides
    @Singleton
    fun provideApiService(): FoodApiService {
        return Retrofit.Builder()
            .baseUrl(FoodApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FoodApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFeedbackApiService(): FeedbackApiService {
        return Retrofit.Builder()
            .baseUrl(FeedbackApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FeedbackApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotesDatabase(@ApplicationContext context: Context): NotesDatabase {
        return Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "notes-database"
        )
            .createFromAsset("notesAssetDatabase.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideFoodDao(db: NotesDatabase): FoodDao {
        return db.foodDao()
    }

    @Provides
    @Singleton
    fun provideMealHistoryDao(db: NotesDatabase): MealHistoryDao {
        return db.mealHistoryDao()
    }

    @Provides
    @Singleton
    fun provideIngredientDao(db: NotesDatabase): IngredientDao {
        return db.ingredientDao()
    }

    @Provides
    @Singleton
    fun provideExerciseDao(db: NotesDatabase): ExerciseDao {
        return db.exerciseDao()
    }

    @Provides
    @Singleton
    fun provideProgramDao(db: NotesDatabase): ProgramDao {
        return db.programDao()
    }

    @Provides
    @Singleton
    fun provideProgramProgressDao(db: NotesDatabase): ProgramProgressDao {
        return db.programProgressDao()
    }

    @Provides
    @Singleton
    fun provideDietRepository(
        foodDao: FoodDao,
        mealHistoryDao: MealHistoryDao,
        ingredientDao: IngredientDao,
        apiService: FoodApiService
    ): DietRepository
    {
        return DietRepository(
            foodDao = foodDao,
            mealHistoryDao = mealHistoryDao,
            ingredientDao = ingredientDao,
            apiService = apiService
        )
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(exerciseDao: ExerciseDao): ExerciseRepository {
        return ExerciseRepository(exerciseDao)
    }

    @Provides
    @Singleton
    fun provideProgramRepository(programDao: ProgramDao, programProgressDao: ProgramProgressDao): ProgramRepository {
        return ProgramRepository(programDao, programProgressDao)
    }

    @Provides
    @Singleton
    fun provideAppProtoDataStore(@ApplicationContext context: Context): DataStore<AppProtoStore> {
        return context.appProtoDataStore
    }

    @Provides
    @Singleton
    fun provideAppProtoDataStoreRepository(dataStore: DataStore<AppProtoStore>): AppProtoDataStoreRepository {
        return AppProtoDataStoreRepository(dataStore)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}