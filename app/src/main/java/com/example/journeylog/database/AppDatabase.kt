package com.example.journeylog.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LogEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): LogDao
}