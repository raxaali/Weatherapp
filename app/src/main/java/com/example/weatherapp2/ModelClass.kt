package com.example.weatherapp2


import com.example.weatherapp2.Currentdata.CurrentWeatherModelClass

import com.example.weatherapp2.Hourlydata.Hourly
import com.example.weatherapp2.pojo.Daily
import com.google.gson.annotations.SerializedName

data class ModelClass(
    @SerializedName("current")
    var currentWeather: CurrentWeatherModelClass,
    var hourly: List<Hourly>,
    val daily: List<Daily>



    )


