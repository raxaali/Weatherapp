package com.example.weatherapp2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp2.AQindex.AQIModelClass
import com.example.weatherapp2.Currentdata.CurrentWeatherModelClass
import com.example.weatherapp2.Database.AQITable
import com.example.weatherapp2.Database.CurrentWeatherTable
import com.example.weatherapp2.Database.DailyTable
import com.example.weatherapp2.Database.HourlyTable
import com.example.weatherapp2.Hourlydata.Hourly
import com.example.weatherapp2.ModelClass
import com.example.weatherapp2.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class APIViewModel(val apiRepository: ApiRepository) : ViewModel() {


    fun SendAPIRequest(lat: Double, lng: Double, areaa: String) {
        Log.e("weatherapp", "SendAPIRequest: send api request from view model")
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.SendAPIRequest(lat, lng, areaa)
        }
    }


    fun Showcrntdata(): LiveData<CurrentWeatherTable> {
        Log.e("weatherapp", "show data from view model")


        val crnt = apiRepository.showCurrentData_frmDB()

        if (crnt != null) {
            Log.e("TAG", "Showcrntdata:crnt live data is not null ")
        } else {
            Log.e("TAG", "Showcrntdata:crnt livevv data is  null")
        }



        return crnt

    }

    fun Showhourlydata(): LiveData<List<HourlyTable>> {
        val hourlylivedata = apiRepository.showHourlyData_frmDb()
        return hourlylivedata
    }

    fun Showdailydata(): LiveData<List<DailyTable>> {
        val dailylivedata = apiRepository.showDailyData_frmDb()
        return dailylivedata
    }


    fun Showaqidata(): LiveData<List<AQITable>> {
        val aqilivedata = apiRepository.showAQIData_frmDB()
        return aqilivedata
    }

    val crntlivedata = apiRepository.crntweather_livedata
    val hourlylivedata = apiRepository.hourly_livedata
    val dailylivedata = apiRepository.daily_livedata
    val aqilivedata: LiveData<List<AQITable>> = apiRepository.aqi_livedata


}