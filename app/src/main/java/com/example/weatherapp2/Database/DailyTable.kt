package com.example.weatherapp2.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DailyTable")
 data class DailyTable(
    @PrimaryKey val dailydt: Int,
    @ColumnInfo( name = "dailydaytemp") val dailydaytemp: Double,
    @ColumnInfo( name = "dailynighttemp") val dailynighttemp: Double,
    @ColumnInfo( name = "dailywind_speed") var dailywind_speed: Double,
    @ColumnInfo( name = "dailyhumidity") var dailyhumidity: Int,
    @ColumnInfo( name = "dailyclouds")var dailyclouds: Int,
    @ColumnInfo( name = "dailyiconid")val dailyiconid: Int,
    @ColumnInfo( name = "dailyicon") val dailyicon: String,
    @ColumnInfo( name = "dailyicondesc")val dailyicondesc: String,
)