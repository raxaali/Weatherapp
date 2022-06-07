package com.example.weatherapp2.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AQITable")
data class AQITable(

    @PrimaryKey   var aqi: Int,
    @ColumnInfo(name = "co")var co: Double,
    @ColumnInfo(name = "o3")var o3: Double,
    @ColumnInfo(name = "no")var no: Double,
    @ColumnInfo(name = "no2")var no2: Double,
    @ColumnInfo(name = "so2")var so2: Double,
    @ColumnInfo(name = "pm2_5")var pm2_5: Double,
    @ColumnInfo(name = "pm10")var pm10: Double,
    @ColumnInfo(name = "nh3")var nh3: Double
)
