package com.example.weatherapp2.Hourlydata

import com.example.weatherapp2.Currentdata.Weather
import com.google.gson.annotations.SerializedName


data class Hourly(
    val dt: Double,
    @SerializedName("temp")
    val hourlytemp: Double,
    @SerializedName("weather")
    var hourlyweathericonlist:List<Weather>,
)
