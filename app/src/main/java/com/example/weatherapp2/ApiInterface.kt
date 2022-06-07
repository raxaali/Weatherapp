package com.example.weatherapp2


import com.example.weatherapp2.AQindex.AQIModelClass
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("onecall")
    suspend  fun GetWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Response<ModelClass>


    @GET("air_pollution")
    suspend fun GetAQIData(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") appid: String
    ): Response<AQIModelClass>




}