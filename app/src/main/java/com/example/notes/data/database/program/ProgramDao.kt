package com.example.notes.data.database.program

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {
    @Query("SELECT * FROM Program")
    fun getAll(): Flow<List<Program>>

    @Query("SELECT * FROM Program WHERE dayOfWeek = :dayOfWeek")
    fun getProgramForToday(dayOfWeek: String): Flow<List<Program>>

    @Query("DELETE FROM Program WHERE dayOfWeek = :dayOfWeek")
    suspend fun dropProgramForDay(dayOfWeek: String)

    @Query("DELETE FROM Program")
    suspend fun dropProgram()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Program)

    @Delete
    suspend fun delete(item:Program)
}