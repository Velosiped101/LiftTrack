package com.velosiped.training.di

import android.content.Context
import androidx.room.Room
import com.velosiped.training.database.TrainingDatabase
import com.velosiped.training.exercise.databasemodel.ExerciseDao
import com.velosiped.training.exercise.repository.ExerciseRepository
import com.velosiped.training.exercise.repositoryimpl.ExerciseRepositoryImpl
import com.velosiped.training.program.databasemodel.ProgramDao
import com.velosiped.training.program.repository.ProgramRepository
import com.velosiped.training.program.repositoryimpl.ProgramRepositoryImpl
import com.velosiped.training.traininghistory.databasemodel.TrainingHistoryDao
import com.velosiped.training.traininghistory.repository.TrainingHistoryRepository
import com.velosiped.training.traininghistory.repositoryimpl.TrainingHistoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object TrainingModule {

    @Provides
    @Singleton
    fun provideTrainingDatabase(
        @ApplicationContext context: Context
    ): TrainingDatabase = Room
        .databaseBuilder(
            context = context,
            klass = TrainingDatabase::class.java,
            name = "training_database"
        )
        .createFromAsset("training_database_asset.db")
        .build()

    @Provides
    @Singleton
    fun provideExerciseDao(db: TrainingDatabase): ExerciseDao = db.exerciseDao()

    @Provides
    @Singleton
    fun provideProgramDao(db: TrainingDatabase): ProgramDao = db.programDao()

    @Provides
    @Singleton
    fun provideProgramProgressDao(db: TrainingDatabase): TrainingHistoryDao = db.trainingHistoryDao()

    @Provides
    @Singleton
    fun provideExerciseRepository(exerciseDao: ExerciseDao): ExerciseRepository =
        ExerciseRepositoryImpl(exerciseDao)

    @Provides
    @Singleton
    fun provideProgramRepository(programDao: ProgramDao): ProgramRepository =
        ProgramRepositoryImpl(programDao)

    @Provides
    @Singleton
    fun provideTrainingHistoryRepository(trainingHistoryDao: TrainingHistoryDao): TrainingHistoryRepository =
        TrainingHistoryRepositoryImpl(trainingHistoryDao)

}