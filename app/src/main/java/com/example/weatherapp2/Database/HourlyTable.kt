package com.example.weatherapp2.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HourlyTable")
data class HourlyTable(
    @PrimaryKey val dt: Double,
    @ColumnInfo(name = "temp") val temp: Double,
    @ColumnInfo(name = "hourlyweathericon") var hourlyweathericon: String,
    @ColumnInfo(name = "hourlyweatherdesc") var hourlyweatherdesc: String,
)
