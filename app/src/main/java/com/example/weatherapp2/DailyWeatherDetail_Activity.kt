package com.example.weatherapp2

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp2.Dailydata.DailyDataAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class DailyWeatherDetail_Activity : AppCompatActivity() {



    private lateinit var dailydetailrecv: RecyclerView
    private lateinit var dailyadapter: DailyDataAdapter

    private lateinit var adView: AdView
    private lateinit var bannnercontainer: FrameLayout
    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = bannnercontainer.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                this,
                adWidth
            )
        }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_weather_detail_)




        bannnercontainer = findViewById(R.id.banner_container)

        val sharepreff: SharedPreferences = getSharedPreferences(
            "Tempratureunits",
            MODE_PRIVATE
        )

        var tempunit: String = sharepreff.getString("tempUnit", "celcius")!!



        dailydetailrecv = findViewById(R.id.dailydetail_rec)
        dailydetailrecv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        dailyadapter = DailyDataAdapter(
            this,
            MainActivity.dailydatalist,
            1,
            tempunit
        )
        dailydetailrecv.adapter = dailyadapter


        val btn_back = findViewById<ImageView>(R.id.back_btn)

        btn_back.setOnClickListener {
            onBackPressed()

        }

        adView = AdView(this)
        bannnercontainer.addView(adView)
        loadBanner()


    }


    @SuppressLint("MissingPermission")
    private fun loadBanner() {
        adView.adUnitId = resources.getString(R.string.bannerid)

        adView.adSize = adSize

        val adRequest = AdRequest
            .Builder().build()

        // Start loading the ad in the background.
        adView.loadAd(adRequest)
    }

}