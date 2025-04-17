package com.example.notes.data.local.program

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notes.utils.DayOfWeek
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {
    @Query("SELECT * FROM Program")
    fun getAll(): Flow<List<Program>>

    @Query("SELECT * FROM Program WHERE id = :id")
    suspend fun getById(id: Int): Program

    @Query("SELECT * FROM Program WHERE dayOfWeek = :dayOfWeek")
    suspend fun getProgramForToday(dayOfWeek: String): List<Program>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Program)

    @Delete
    suspend fun delete(item:Program)
}