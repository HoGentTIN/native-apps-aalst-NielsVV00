package com.example.project3pt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.utils.Converters

@Database(entities = [Wedstrijd::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database3PT : RoomDatabase() {
    abstract val wedstrijdDao: WedstrijdDao

    companion object {
        @Volatile
        private var INSTANCE: Database3PT? = null

        fun getInstance(context: Context): Database3PT {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        Database3PT::class.java,
                        "3pt_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}