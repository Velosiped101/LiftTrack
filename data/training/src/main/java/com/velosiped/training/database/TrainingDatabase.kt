package com.velosiped.training.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.velosiped.training.exercise.databasemodel.ExerciseDao
import com.velosiped.training.exercise.databasemodel.ExerciseEntity
import com.velosiped.training.program.databasemodel.ProgramDao
import com.velosiped.training.program.databasemodel.ProgramEntity
import com.velosiped.training.traininghistory.databasemodel.TrainingHistoryDao
import com.velosiped.training.traininghistory.databasemodel.TrainingHistoryEntity

@Database(
    entities = [ExerciseEntity::class, ProgramEntity::class, TrainingHistoryEntity::class],
    version = 1
)
internal abstract class TrainingDatabase: RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun programDao(): ProgramDao
    abstract fun trainingHistoryDao(): TrainingHistoryDao
}