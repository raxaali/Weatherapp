package com.example.weatherapp2.Database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrentWeatherTable(
    @PrimaryKey val temp: Double,
    @ColumnInfo(name = "clouds") val clouds: Double,
    @ColumnInfo(name = "wind_speed") val wind_speed: Double,
    @ColumnInfo(name = "humidity") val humidity: Double,
    @ColumnInfo(name = "dt") val dt: Long,
    @ColumnInfo(name = "feels_like") var feels_like: Double,
    @ColumnInfo(name = "uvi") var uvi: Double,
    @ColumnInfo(name = "visibility") var visibility: Double,
    @ColumnInfo(name = "pressure") var pressure: Double,
    @ColumnInfo(name = "dew_point") var dew_point: Double,
    @ColumnInfo(name = "currenticonid") var currenticonid: Int,
    @ColumnInfo(name = "weathericon") var weathericon: String,
    @ColumnInfo(name = "weatherdesc") var weatherdesc: String,
    @ColumnInfo(name = "currentsunrise") var currentsunrise:Long,
    @ColumnInfo(name = "currentsunset") var currentsunset:Long,
    @ColumnInfo(name = "area") var area: String,




)


