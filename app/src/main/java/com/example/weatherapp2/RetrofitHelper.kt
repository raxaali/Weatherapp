package com.example.weatherapp2


import android.os.Build
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {



    fun getRetrofItinstance(): Retrofit {

        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
            return Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }else{
            return Retrofit.Builder().baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    }
}