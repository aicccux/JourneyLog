package com.example.journeylog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LogEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "log_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
    abstract fun logDao(): LogDao
}