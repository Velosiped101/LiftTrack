package com.velosiped.diet.di

import android.content.Context
import androidx.room.Room
import com.velosiped.diet.database.DietDatabase
import com.velosiped.diet.food.databasemodel.FoodDao
import com.velosiped.diet.food.datasource.api.foodApi.FoodApiConstants
import com.velosiped.diet.food.datasource.api.foodApi.FoodApiService
import com.velosiped.diet.food.repository.FoodRepository
import com.velosiped.diet.food.repositoryimpl.FoodRepositoryImpl
import com.velosiped.diet.ingredient.databasemodel.IngredientDao
import com.velosiped.diet.ingredient.repository.IngredientRepository
import com.velosiped.diet.ingredient.repositoryimpl.IngredientRepositoryImpl
import com.velosiped.diet.mealhistory.databasemodel.MealHistoryDao
import com.velosiped.diet.mealhistory.repository.MealHistoryRepository
import com.velosiped.diet.mealhistory.repositoryimpl.MealHistoryRepositoryImpl
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
internal object DietModule {

    @Provides
    @Singleton
    fun provideDietDatabase(@ApplicationContext context: Context): DietDatabase = Room
        .databaseBuilder(
            context,
            DietDatabase::class.java,
            "diet_database"
        )
        .createFromAsset("diet_database_asset.db")
        .build()

    @Provides
    @Singleton
    fun provideFoodApiService(): FoodApiService = Retrofit.Builder()
        .baseUrl(FoodApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FoodApiService::class.java)

    @Provides
    @Singleton
    fun provideFoodDao(db: DietDatabase): FoodDao = db.foodDao()

    @Provides
    @Singleton
    fun provideMealHistoryDao(db: DietDatabase): MealHistoryDao = db.mealHistoryDao()

    @Provides
    @Singleton
    fun provideIngredientDao(db: DietDatabase): IngredientDao = db.ingredientDao()

    @Provides
    @Singleton
    fun provideFoodRepository(
        foodDao: FoodDao,
        apiService: FoodApiService
    ): FoodRepository = FoodRepositoryImpl(
        foodDao = foodDao,
        apiService = apiService
    )

    @Provides
    @Singleton
    fun provideMealHistoryRepository(
        mealHistoryDao: MealHistoryDao
    ): MealHistoryRepository = MealHistoryRepositoryImpl(
        mealHistoryDao = mealHistoryDao
    )

    @Provides
    @Singleton
    fun provideIngredientRepository(
        ingredientDao: IngredientDao
    ): IngredientRepository = IngredientRepositoryImpl(
        ingredientDao = ingredientDao
    )

}