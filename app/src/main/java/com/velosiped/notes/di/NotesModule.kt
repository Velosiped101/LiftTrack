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
import com.velosiped.notes.data.repository.diet.DietRepositoryImpl
import com.velosiped.notes.data.repository.exercise.ExerciseRepositoryImpl
import com.velosiped.notes.data.repository.program.ProgramRepositoryImpl
import com.velosiped.notes.data.repository.tempprogress.AppProtoDataStoreRepositoryImpl
import com.velosiped.notes.data.repository.tempprogress.appProtoDataStore
import com.velosiped.notes.domain.usecase.diet.DietUseCase
import com.velosiped.notes.domain.usecase.diet.addmeal.ConfirmMealAdditionUseCase
import com.velosiped.notes.domain.usecase.diet.addmeal.ManagePickedFoodListUseCase
import com.velosiped.notes.domain.usecase.diet.addmeal.ObserveTotalNutrientsUseCase
import com.velosiped.notes.domain.usecase.diet.addmeal.SearchFoodUseCase
import com.velosiped.notes.domain.usecase.diet.addmeal.ValidateMassUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.AddFoodToDbUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.CreateImageFileUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.DeleteFoodFromDbUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.DeleteImageFileUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.FoodClickedInDeleteModeUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.GetFoodInformationUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.ObserveFoodListUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.ValidateFoodInputUseCase
import com.velosiped.notes.domain.usecase.diet.newrecipe.CreateNewFoodUseCase
import com.velosiped.notes.domain.usecase.diet.newrecipe.CreateNewIngredientsUseCase
import com.velosiped.notes.domain.usecase.diet.newrecipe.SearchForIngredientsUseCase
import com.velosiped.notes.domain.usecase.main.CheckForProgramUpdateUseCase
import com.velosiped.notes.domain.usecase.main.CycleGraphDataUseCase
import com.velosiped.notes.domain.usecase.main.MainUseCase
import com.velosiped.notes.domain.usecase.main.ObserveMainScreenInformationUseCase
import com.velosiped.notes.domain.usecase.main.ResetProgramProgressUseCase
import com.velosiped.notes.domain.usecase.settings.GetUserSettingsUseCase
import com.velosiped.notes.domain.usecase.settings.SettingsUseCase
import com.velosiped.notes.domain.usecase.settings.UpdateUserSettingsUseCase
import com.velosiped.notes.domain.usecase.statistics.GetGraphDataUseCase
import com.velosiped.notes.domain.usecase.training.TrainingUseCase
import com.velosiped.notes.domain.usecase.training.programedit.GetExerciseListUseCase
import com.velosiped.notes.domain.usecase.training.programedit.ManageDayInProgramUseCase
import com.velosiped.notes.domain.usecase.training.programedit.ManageExerciseInProgramUseCase
import com.velosiped.notes.domain.usecase.training.programedit.ObserveProgramUseCase
import com.velosiped.notes.domain.usecase.training.programexec.FinishTrainingUseCase
import com.velosiped.notes.domain.usecase.training.programexec.GetTrainingStateUseCase
import com.velosiped.notes.domain.usecase.training.programexec.UpdateStoredProgressUseCase
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
    ): DietRepositoryImpl
    {
        return DietRepositoryImpl(
            foodDao = foodDao,
            mealHistoryDao = mealHistoryDao,
            ingredientDao = ingredientDao,
            apiService = apiService
        )
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(exerciseDao: ExerciseDao): ExerciseRepositoryImpl {
        return ExerciseRepositoryImpl(exerciseDao)
    }

    @Provides
    @Singleton
    fun provideProgramRepository(programDao: ProgramDao, programProgressDao: ProgramProgressDao): ProgramRepositoryImpl {
        return ProgramRepositoryImpl(programDao, programProgressDao)
    }

    @Provides
    @Singleton
    fun provideAppProtoDataStore(@ApplicationContext context: Context): DataStore<AppProtoStore> {
        return context.appProtoDataStore
    }

    @Provides
    @Singleton
    fun provideAppProtoDataStoreRepository(dataStore: DataStore<AppProtoStore>): AppProtoDataStoreRepositoryImpl {
        return AppProtoDataStoreRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun provideDietUseCase(
        dietRepository: DietRepositoryImpl,
        protoDataStoreRepository: AppProtoDataStoreRepositoryImpl
    ): DietUseCase = DietUseCase(
        validateMassUseCase = ValidateMassUseCase(),
        searchFoodUseCase = SearchFoodUseCase(dietRepository),
        confirmMealAdditionUseCase = ConfirmMealAdditionUseCase(dietRepository),
        observeTotalNutrientsUseCase = ObserveTotalNutrientsUseCase(
            dietRepository = dietRepository,
            protoDataStoreRepository = protoDataStoreRepository
        ),
        managePickedFoodListUseCase = ManagePickedFoodListUseCase(),
        foodClickedInDeleteModeUseCase = FoodClickedInDeleteModeUseCase(),
        getFoodInformationUseCase = GetFoodInformationUseCase(),
        addFoodToDbUseCase = AddFoodToDbUseCase(dietRepository),
        createNewFoodUseCase = CreateNewFoodUseCase(dietRepository),
        createImageFileUseCase = CreateImageFileUseCase(),
        deleteImageFileUseCase = DeleteImageFileUseCase(),
        observeFoodListUseCase = ObserveFoodListUseCase(dietRepository),
        validateFoodInputUseCase = ValidateFoodInputUseCase(),
        deleteFoodFromDbUseCase = DeleteFoodFromDbUseCase(dietRepository),
        createNewIngredientsUseCase = CreateNewIngredientsUseCase(dietRepository),
        searchForIngredientsUseCase = SearchForIngredientsUseCase(dietRepository)
    )

    @Provides
    fun provideTrainingUseCase(
        programRepository: ProgramRepositoryImpl,
        exerciseRepository: ExerciseRepositoryImpl,
        appProtoDataStoreRepository: AppProtoDataStoreRepositoryImpl
    ): TrainingUseCase = TrainingUseCase(
        finishTrainingUseCase = FinishTrainingUseCase(appProtoDataStoreRepository, programRepository),
        getTrainingStateUseCase = GetTrainingStateUseCase(appProtoDataStoreRepository, programRepository),
        manageExerciseInProgramUseCase = ManageExerciseInProgramUseCase(programRepository),
        getExerciseListUseCase = GetExerciseListUseCase(exerciseRepository),
        manageDayInProgramUseCase = ManageDayInProgramUseCase(programRepository),
        updateStoredProgressUseCase = UpdateStoredProgressUseCase(appProtoDataStoreRepository),
        observeProgramUseCase = ObserveProgramUseCase(programRepository)
    )

    @Provides
    fun provideGetGraphDataUseCase(
        programRepository: ProgramRepositoryImpl
    ): GetGraphDataUseCase = GetGraphDataUseCase(programRepository)

    @Provides
    fun provideSettingsUseCase(
        appProtoDataStoreRepository: AppProtoDataStoreRepositoryImpl
    ): SettingsUseCase = SettingsUseCase(
        getUserSettingsUseCase = GetUserSettingsUseCase(appProtoDataStoreRepository),
        updateUserSettingsUseCase = UpdateUserSettingsUseCase(appProtoDataStoreRepository)
    )

    @Provides
    fun provideMainUseCase(
        dietRepository: DietRepositoryImpl,
        getGraphDataUseCase: GetGraphDataUseCase,
        programRepository: ProgramRepositoryImpl,
        appProtoDataStoreRepository: AppProtoDataStoreRepositoryImpl
    ): MainUseCase = MainUseCase(
        checkForProgramUpdateUseCase = CheckForProgramUpdateUseCase(
            programRepository,
            appProtoDataStoreRepository
        ),
        cycleGraphDataUseCase = CycleGraphDataUseCase(),
        observeMainScreenInformationUseCase = ObserveMainScreenInformationUseCase(
            dietRepository,
            programRepository,
            appProtoDataStoreRepository,
            getGraphDataUseCase
        ),
        resetProgramProgressUseCase = ResetProgramProgressUseCase(appProtoDataStoreRepository)
    )
}