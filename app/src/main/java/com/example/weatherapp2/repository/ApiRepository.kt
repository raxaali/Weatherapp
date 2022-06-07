package com.example.weatherapp2.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp2.AQindex.AQIList
import com.example.weatherapp2.ApiInterface
import com.example.weatherapp2.Currentdata.CurrentWeatherModelClass
import com.example.weatherapp2.Database.*
import com.example.weatherapp2.Hourlydata.Hourly
import com.example.weatherapp2.pojo.Daily
import kotlinx.coroutines.delay

class ApiRepository(
    private val apiInterface: ApiInterface,
    private val weatherDBobj: WeatherDB,

    ) {
    private val crntweather_mutablelivedata = MutableLiveData<CurrentWeatherTable>()
    val crntweather_livedata = crntweather_mutablelivedata

    private val hourly_mutablelivedata = MutableLiveData<List<HourlyTable>>()
    val hourly_livedata = hourly_mutablelivedata

    private val daily_mutablelivedata = MutableLiveData<List<DailyTable>>()
    val daily_livedata = daily_mutablelivedata


    private val aqi_mutablelivedata = MutableLiveData<List<AQITable>>()
    val aqi_livedata = aqi_mutablelivedata


    @SuppressLint("MissingPermission")
    suspend fun SendAPIRequest(lat: Double, lng: Double, areaa: String) {
        Log.e("weatherapp", "SendAPIRequest: hit api from repository")

        try {
            val result = apiInterface.GetWeatherData(lat, lng, "e7467c98c89de5379b9e0702f361f602")

            val aqi_result = apiInterface.GetAQIData(lat, lng, "e7467c98c89de5379b9e0702f361f602")

            if (result.isSuccessful) {
                Log.e("weatherapp", "SendAPIRequest: response is   ${result.isSuccessful}")

                if (result.body() != null) {
                    storeCurrentData_inDB(result.body()!!.currentWeather, areaa)
                    storeHourlyData_inDB(result.body()!!.hourly)
                    storeDailyData_inDB(result.body()!!.daily)
                }

                showCurrentData_frmDB()
                showHourlyData_frmDb()
                showDailyData_frmDb()

            } else {
                Log.e("weatherapp", "SendAPIRequest: response is   ${result.isSuccessful}")
                // only show data from db
                showCurrentData_frmDB()
                showHourlyData_frmDb()
                showDailyData_frmDb()
            }

            if (aqi_result.isSuccessful) {
                if (aqi_result.body() != null) {
                    Log.e(
                        "weatherapp",
                        "SendAPIRequest: aqi response ${aqi_result.body()!!.list[0].main.aqi}",
                    )
                    storeAQIData_inDB(aqi_result.body()!!.list)
                }
                showAQIData_frmDB()
            } else {
                showAQIData_frmDB()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun storeCurrentData_inDB(curreweather_obj: CurrentWeatherModelClass, areaa: String) {

        if (weatherDBobj.weatherDao().getCurrentDatafrmDB().isNotEmpty()) {
            weatherDBobj.weatherDao().deleteCurrentWeatherTable()
        }
        val currentweathertable_obj = CurrentWeatherTable(
            curreweather_obj.temp,
            curreweather_obj.clouds,
            curreweather_obj.wind_speed,
            curreweather_obj.humidity,
            curreweather_obj.dt,
            curreweather_obj.feels_like,
            curreweather_obj.uvi,
            curreweather_obj.visibility,
            curreweather_obj.pressure,
            curreweather_obj.dew_point,
            curreweather_obj.crntweathericonlist[0].id,
            curreweather_obj.crntweathericonlist[0].icon,
            curreweather_obj.crntweathericonlist[0].description,
            curreweather_obj.currentsunrise,
            curreweather_obj.currentsunset,
            areaa
        )

        weatherDBobj.weatherDao().insertCurrentData(currentweathertable_obj)

        Log.e("weatherapp", "storeCurrentData_inDB: data stored in db ")


    }

    fun showCurrentData_frmDB(): LiveData<CurrentWeatherTable> {

        /* val crntweather_mutablelivedata = MutableLiveData<CurrentWeatherTable>()*/

        if (weatherDBobj.weatherDao().getCurrentDatafrmDB().isNotEmpty()) {

            Log.e("weatherapp", "showCurrentData_frmDB: db is not empty, fetching crnt data frm db")
            val currentweather_dblist = weatherDBobj.weatherDao().getCurrentDatafrmDB()
            val currentWeatherTable = CurrentWeatherTable(
                currentweather_dblist[0].temp,
                currentweather_dblist[0].clouds,
                currentweather_dblist[0].wind_speed,
                currentweather_dblist[0].humidity,
                currentweather_dblist[0].dt,
                currentweather_dblist[0].feels_like,
                currentweather_dblist[0].uvi,
                currentweather_dblist[0].visibility,
                currentweather_dblist[0].pressure,
                currentweather_dblist[0].dew_point,
                currentweather_dblist[0].currenticonid,
                currentweather_dblist[0].weathericon,
                currentweather_dblist[0].weatherdesc,
                currentweather_dblist[0].currentsunrise,
                currentweather_dblist[0].currentsunset,
                currentweather_dblist[0].area,
            )

            crntweather_mutablelivedata.postValue(currentWeatherTable)
        }

        return crntweather_livedata
    }

    private fun storeHourlyData_inDB(hourly: List<Hourly>) {

        if (weatherDBobj.weatherDao().getHourlyDatafrmDB().isNotEmpty()) {
            weatherDBobj.weatherDao().deleteHourlyTable()
        }

        for (i in hourly.indices) {
            val hourlytable_obj = HourlyTable(
                hourly[i].dt,
                hourly[i].hourlytemp,
                hourly[i].hourlyweathericonlist[0].icon,
                hourly[i].hourlyweathericonlist[0].description
            )

            weatherDBobj.weatherDao().insertHourlyData(hourlytable_obj)
        }


    }

    fun showHourlyData_frmDb(): LiveData<List<HourlyTable>> {

        /* val hourly_mutablelivedata = MutableLiveData<List<HourlyTable>>()*/

        if (weatherDBobj.weatherDao().getHourlyDatafrmDB().isNotEmpty()) {
            hourly_mutablelivedata.postValue(weatherDBobj.weatherDao().getHourlyDatafrmDB())
        }


        return hourly_livedata
    }

    private fun storeDailyData_inDB(daily: List<Daily>) {

        if (weatherDBobj.weatherDao().getDailyDatafrmDB().isNotEmpty()) {
            weatherDBobj.weatherDao().deleteDailyTable()
        }

        for (i in daily.indices) {

            val dailytable_obj = DailyTable(
                daily[i].dt,
                daily[i].temp.day,
                daily[i].temp.night,
                daily[i].wind_speed,
                daily[i].humidity,
                daily[i].clouds,
                daily[i].weather[0].id,
                daily[i].weather[0].icon,
                daily[i].weather[0].description,
            )
            weatherDBobj.weatherDao().insertDailyData(dailytable_obj)
        }


    }

    fun showDailyData_frmDb(): MutableLiveData<List<DailyTable>> {

        /*  val daily_mutablelivedata = MutableLiveData<List<DailyTable>>()*/

        if (weatherDBobj.weatherDao().getDailyDatafrmDB().isNotEmpty()) {
            daily_mutablelivedata.postValue(weatherDBobj.weatherDao().getDailyDatafrmDB())
        }

        return daily_livedata
    }


    private fun storeAQIData_inDB(aqilist: List<AQIList>) {

        if (weatherDBobj.weatherDao().getAQIDatafrmDB().isNotEmpty()) {
            weatherDBobj.weatherDao().deleteAQITable()
        }

        for (i in aqilist.indices) {
            val aqi_tableobj = AQITable(
                aqilist[i].main.aqi,
                aqilist[i].components.co,
                aqilist[i].components.o3,
                aqilist[i].components.no,
                aqilist[i].components.no2,
                aqilist[i].components.so2,
                aqilist[i].components.pm2_5,
                aqilist[i].components.pm10,
                aqilist[i].components.nh3
            )

            weatherDBobj.weatherDao().insertAQIData(aqi_tableobj)
        }
    }

    fun showAQIData_frmDB(): MutableLiveData<List<AQITable>> {

        // val aqi_mutablelivedata = MutableLiveData<List<AQITable>>()

        if (weatherDBobj.weatherDao().getAQIDatafrmDB().isNotEmpty()) {
            aqi_mutablelivedata.postValue(weatherDBobj.weatherDao().getAQIDatafrmDB())

        }


        return aqi_livedata
    }

}