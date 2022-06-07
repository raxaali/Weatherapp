package com.example.weatherapp2.pojo

import com.example.weatherapp2.Currentdata.Weather

data class Daily(
    val dt: Int,
    val temp: Temp,
    val wind_speed: Double,
    val humidity: Int,
    val clouds: Int,
    val weather: List<Weather>,



    val dew_point: Double,
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val sunrise: Int,
    val sunset: Int,
    val uvi: Double,
    val wind_deg: Int,
    val wind_gust: Double,

)