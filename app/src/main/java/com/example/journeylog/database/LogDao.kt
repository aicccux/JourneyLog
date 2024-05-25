package com.example.journeylog.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import java.io.File

@Dao
interface LogDao {
    @Query("SELECT * FROM log_table ORDER BY date DESC")
    suspend fun getAll(): Flow<List<LogEntry>>

    suspend fun getAllWithFiles(photoFolder: File): List<Log> {
        return getAll().single().map { Log.fromLogEntry(it, photoFolder) }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: LogEntry)

    @Delete
    suspend fun delete(log: LogEntry)
}