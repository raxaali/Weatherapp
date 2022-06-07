package com.example.weatherapp2.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import retrofit2.Response

@Dao
interface WeatherDao {

    @Insert
     fun insertCurrentData(currentWeatherlist_db: CurrentWeatherTable)

    @Insert
    fun insertHourlyData(hourlyTable: HourlyTable)

    @Insert
    fun insertDailyData(dailyTable: DailyTable)

    @Insert
    fun insertAQIData(aqiTable: AQITable)


    @Query("SELECT * FROM CurrentWeatherTable")
    fun getCurrentDatafrmDB(): List<CurrentWeatherTable>

    @Query("SELECT * FROM HourlyTable")
    fun getHourlyDatafrmDB(): List<HourlyTable>

    @Query("SELECT * FROM DailyTable")
    fun getDailyDatafrmDB(): List<DailyTable>


    @Query("SELECT * FROM AQITable")
    fun getAQIDatafrmDB(): List<AQITable>

    @Query("DELETE  FROM CurrentWeatherTable")
    fun deleteCurrentWeatherTable()

    @Query("DELETE  FROM HourlyTable")
    fun deleteHourlyTable()

    @Query("DELETE  FROM DailyTable")
    fun deleteDailyTable()

    @Query("DELETE  FROM AQITable")
    fun deleteAQITable()
}