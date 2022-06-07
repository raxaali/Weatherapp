package com.example.weatherapp2.Currentdata

import com.google.gson.annotations.SerializedName


data class CurrentWeatherModelClass(

    val temp: Double,
    val clouds: Double,
    val wind_speed: Double,
    val humidity: Double,
    val dt: Long,
    var feels_like: Double,
    var uvi: Double,
    var visibility: Double,
    var pressure: Double,
    var dew_point: Double,
   @SerializedName("weather")
    var crntweathericonlist:List<Weather>,

    @SerializedName("sunrise")
    var currentsunrise:Long,

    @SerializedName("sunset")
    var currentsunset:Long,



)
