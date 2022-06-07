package com.example.weatherapp2.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CurrentWeatherTable::class, HourlyTable::class, AQITable::class, DailyTable::class],
    version = 10
)
abstract class WeatherDB : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao


    companion object {
        @Volatile
        private var dbinstance: WeatherDB? = null

        fun getDB(context: Context): WeatherDB {

            if (dbinstance == null) {
                synchronized(this) {
                    dbinstance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDB::class.java,
                        "WeatherDatabase"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return dbinstance!!
        }

    }
}