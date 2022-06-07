package com.example.weatherapp2.Hourlydata

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp2.Database.HourlyTable
import com.example.weatherapp2.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourlyDataAdapter(
    private val context: Context,
    private val hourdatalist: List<HourlyTable>,
    private val timeformat: String?,
    private val tempformat: String,
) :
    RecyclerView.Adapter<HourlyDataAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.hourly_single_row, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val temp_incelcius: Int = ((hourdatalist[position].temp) - 273.15).roundToInt()
        val temp_infaren: Int = ((temp_incelcius * 1.8) + 32).roundToInt()

        if (tempformat.equals("celcius")) {
            holder.tvhourlytemp.text = "$temp_incelcius°c"
        }
        else if (tempformat.equals("farenheit")) {
            holder.tvhourlytemp.text = "$temp_infaren°f"

        }


        val tempdate: Double = hourdatalist[position].dt

        if (timeformat.equals("12hr")) {
            val date = Date(tempdate.toLong() * 1000)
            val sdf: DateFormat = SimpleDateFormat("  hh:mm a", Locale.getDefault())
            sdf.timeZone = TimeZone.getDefault()
            val formatted: String = sdf.format(date)
            holder.tvhourlytime.text = formatted
        } else if (timeformat.equals("24hr")) {
            val date = Date(tempdate.toLong() * 1000)
            val sdf: DateFormat = SimpleDateFormat("  HH:mm a", Locale.getDefault())
            sdf.timeZone = TimeZone.getDefault()
            val formatted: String = sdf.format(date)
            holder.tvhourlytime.text = formatted
        }

        if (Build.VERSION.SDK_INT > 23) {

            holder.tvhourlyicon.load( "https://openweathermap.org/img/w/${hourdatalist[position].hourlyweathericon}.png"
            )

        } else {
            holder.tvhourlyicon.load("http://openweathermap.org/img/w/${hourdatalist[position].hourlyweathericon}.png")

        }
        holder.tvhourlyicon.getLayoutParams().width = 100
        holder.tvhourlyicon.getLayoutParams().height = 100





    }

    override fun getItemCount(): Int {
        return hourdatalist.size

    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val tvhourlytemp = itemview.findViewById<TextView>(R.id.tv_hourly_temp)!!
        val tvhourlytime = itemview.findViewById<TextView>(R.id.tv_hourly_time)!!
        val tvhourlyicon = itemview.findViewById<ImageView>(R.id.tv_hourly_icon)!!

    }
}