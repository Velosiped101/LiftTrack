package com.example.notes.data.database.saveddata.programProgress

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramProgressDao {
    @Query("SELECT * from ProgramProgress")
    fun getAll(): Flow<List<ProgramProgress>>

    @Insert
    suspend fun insert(programProgress: ProgramProgress)
}