package com.example.journeylog.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LogEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object{
        const val DB_NAME="journey-log"
    }
    abstract fun getDao(): LogDao
}