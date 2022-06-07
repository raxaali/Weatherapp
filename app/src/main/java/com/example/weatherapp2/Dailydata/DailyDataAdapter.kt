package com.example.weatherapp2.Dailydata

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.example.weatherapp2.Database.DailyTable
import com.example.weatherapp2.R
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.math.roundToInt


private const val daily_type: Int = 0
private const val dailydetail_type: Int = 1


class DailyDataAdapter(
    val context: Context,
    val dailydatalist: List<DailyTable>,
    var type: Int,
    private val tempformat: String,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == daily_type) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.hourly_single_row, parent, false)
            return DailyViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.dailysinglerow, parent, false)
            return DailyDetailViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DailyViewHolder) {

            // getting day of the week
            val temp_date: Int = dailydatalist.get(position).dailydt
            val date = Date(temp_date.toLong() * 1000)
            val sdf: DateFormat = SimpleDateFormat("  EEEE", Locale.getDefault())
            val formatted: String = sdf.format(date)
            holder.tv_hourly_time.text = "$formatted"

            // getting temprature of the week day

            val tmp_incelcius: Int = ((dailydatalist.get(position).dailydaytemp) - 273.15).roundToInt()
            val temp_infarenheit = ((tmp_incelcius * 1.8) + 32).toInt()

            if (tempformat.equals("celcius")){
                holder.tv_hourly_temp.text = "${tmp_incelcius}°c"
            }
            else   if (tempformat.equals("farenheit")){
                holder.tv_hourly_temp.text = "${temp_infarenheit}°f "

            }




            if (Build.VERSION.SDK_INT==30){

                holder.tv_hourly_icon.load("https://openweathermap.org/img/w/${dailydatalist[position].dailyicon}.png")

            }
            else{
                holder.tv_hourly_icon.load("http://openweathermap.org/img/w/${dailydatalist[position].dailyicon}.png")

            }

            holder.tv_hourly_icon.getLayoutParams().width = 100
            holder.tv_hourly_icon.getLayoutParams().height = 100



        } else if (holder is DailyDetailViewHolder) {

            holder.tv_dailyhumidity.text = "${dailydatalist.get(position).dailyhumidity}mm"
            holder.tv_dailywind_speed.text = "${dailydatalist.get(position).dailywind_speed}km"
            holder.tvdailyrainchance.text = "${dailydatalist.get(position).dailyclouds}%"

            holder.tv_dailyhumiditynight.text = "${dailydatalist.get(position).dailyhumidity}mm"
            holder.tv_dailywind_speednight.text = "${dailydatalist.get(position).dailywind_speed}km"
            holder.tvdailyrainchancenight.text = "${dailydatalist.get(position).dailyclouds}%"





            val tmp_incelciusday: Int = ((dailydatalist.get(position).dailydaytemp) - 273.15).roundToInt()
            val tmp_incelciusnight: Int = ((dailydatalist.get(position).dailynighttemp) - 273.15).roundToInt()
            val temp_infarenheitday = ((tmp_incelciusday * 1.8) + 32).toInt()
            val temp_infarenheitnight = ((tmp_incelciusnight * 1.8) + 32).toInt()

            if (tempformat.equals("celcius")){

                holder.tv_nightweatherdesc.text =
                    " ${dailydatalist[position].dailyicondesc},${tmp_incelciusnight}°c"
                holder.tv_dayweatherdesc.text =
                    " ${dailydatalist[position].dailyicondesc}, ${tmp_incelciusday}°c"
            }
            else   if (tempformat.equals("farenheit")){

                holder.tv_nightweatherdesc.text =
                    " ${dailydatalist[position].dailyicondesc},${temp_infarenheitnight}°f"
                holder.tv_dayweatherdesc.text =
                    " ${dailydatalist[position].dailyicondesc}, ${temp_infarenheitday}°f"

            }



            val temptime = Date((dailydatalist.get(position).dailydt.toLong()) * 1000)
            val sdf: DateFormat = SimpleDateFormat("EEEE \n MMM,dd", Locale.getDefault())
            holder.tv_dailyday.text = sdf.format(temptime)

            holder.tv_dailyweatherdesc.text = dailydatalist[position].dailyicondesc
            holder.tv_dailyweatherdescnight.text = dailydatalist[position].dailyicondesc



            if (Build.VERSION.SDK_INT>23){


                holder.tv_dailyicon.load("https://openweathermap.org/img/w/${dailydatalist[position].dailyicon}.png")

            }
            else{
                holder.tv_dailyicon.load("http://openweathermap.org/img/w/${dailydatalist[position].dailyicon}.png")

            }



        }
    }


    override fun getItemCount(): Int {
        return dailydatalist.size

    }

    override fun getItemViewType(position: Int): Int {
        val viewtype = if (type == daily_type) daily_type else dailydetail_type

        return viewtype
    }

    class DailyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val tv_hourly_temp = itemview.findViewById<TextView>(R.id.tv_hourly_temp)
        val tv_hourly_time = itemview.findViewById<TextView>(R.id.tv_hourly_time)
        val tv_hourly_icon = itemview.findViewById<ImageView>(R.id.tv_hourly_icon)

    }


    class DailyDetailViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val tv_dailyday = itemview.findViewById<TextView>(R.id.tv_dailyday)
        val tv_dailyicon = itemview.findViewById<ImageView>(R.id.daily_weathericon)
        val tv_dailyweatherdesc = itemview.findViewById<TextView>(R.id.daily_weatherdesc)
        val tv_dailyweatherdescnight = itemview.findViewById<TextView>(R.id.daily_nightweatherdesc)
        val tv_nightweatherdesc = itemview.findViewById<TextView>(R.id.tv_nightweather_desc)
        val tv_dayweatherdesc = itemview.findViewById<TextView>(R.id.tv_dayweather_desc)
        val tvdailyrainchance = itemview.findViewById<TextView>(R.id.tv_dailyrain_chance)
        val tv_dailywind_speed = itemview.findViewById<TextView>(R.id.tv_dailywind_speed)
        val tv_dailyhumidity = itemview.findViewById<TextView>(R.id.tv_dailyhumidity)

        val tvdailyrainchancenight = itemview.findViewById<TextView>(R.id.tv_dailyrain_chancenight)
        val tv_dailywind_speednight = itemview.findViewById<TextView>(R.id.tv_dailywind_speednight)
        val tv_dailyhumiditynight = itemview.findViewById<TextView>(R.id.tv_dailyhumiditynight)
    }


}