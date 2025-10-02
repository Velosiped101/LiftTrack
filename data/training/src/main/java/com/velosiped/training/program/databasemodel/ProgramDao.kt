package com.velosiped.training.program.databasemodel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProgramDao {
    @Query("SELECT * FROM ProgramEntity")
    fun getAll(): Flow<List<ProgramEntity>>

    @Query("SELECT * FROM ProgramEntity WHERE day = :dayOfWeek")
    fun getProgramForToday(dayOfWeek: String): Flow<List<ProgramEntity>>

    @Query("DELETE FROM ProgramEntity WHERE day = :dayOfWeek")
    suspend fun dropProgramForDay(dayOfWeek: String)

    @Query("DELETE FROM ProgramEntity")
    suspend fun dropProgram()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ProgramEntity)

    @Delete
    suspend fun delete(item:ProgramEntity)
}