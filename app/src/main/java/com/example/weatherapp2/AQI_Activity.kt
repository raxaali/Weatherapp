package com.example.weatherapp2

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest

class AQI_Activity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a_q_i_)


        // native ad
        val adLoader = AdLoader.Builder(this, resources.getString(R.string.native_id))
            .forNativeAd { nativeAd ->
                val styles =
                    NativeTemplateStyle.Builder().build()
                val template: TemplateView = findViewById(R.id.my_template)
                template.setStyles(styles)
                template.setNativeAd(nativeAd)
            }
            .build()

        adLoader.loadAd(AdRequest.Builder().build())


        val aq_value = findViewById<TextView>(R.id.tv_aqvalue)
        val aqi_indextitle = findViewById<TextView>(R.id.tv_aqi_index)
        val aq_detaildesc = findViewById<TextView>(R.id.aq_detaildesc)
        val aq_sensitivedesc = findViewById<TextView>(R.id.aq_sensitivedesc)

        val pollutant1_value = findViewById<TextView>(R.id.pollutant1_value)
        val pollutant2_value = findViewById<TextView>(R.id.pollutant2_value)
        val pollutant3_value = findViewById<TextView>(R.id.pollutant3_value)
        val pollutant4_value = findViewById<TextView>(R.id.pollutant4_value)
        val pollutant5_value = findViewById<TextView>(R.id.pollutant5_value)


        val pollutant1_desc = findViewById<TextView>(R.id.pollutant1_desc)
        val pollutant2_desc = findViewById<TextView>(R.id.pollutant2_desc)
        val pollutant3_desc = findViewById<TextView>(R.id.pollutant3_desc)
        val pollutant4_desc = findViewById<TextView>(R.id.pollutant4_desc)
        val pollutant5_desc = findViewById<TextView>(R.id.pollutant5_desc)

        val aqdetail_seekbar = findViewById<ProgressBar>(R.id.aqdetail_seekbar)

        val img_pollutant1 = findViewById<Button>(R.id.img_pollutant1)
        val img_pollutant2 = findViewById<Button>(R.id.img_pollutant2)
        val img_pollutant3 = findViewById<Button>(R.id.img_pollutant3)
        val img_pollutant4 = findViewById<Button>(R.id.img_pollutant4)
        val img_pollutant5 = findViewById<Button>(R.id.img_pollutant5)

        aq_value.text = "${MainActivity.aqivalue}"
        pollutant1_value.text = "${MainActivity.no2}ug/m3"
        pollutant2_value.text = "${MainActivity.o3}ug/m3"
        pollutant3_value.text = "${MainActivity.so2}ug/m3"
        pollutant4_value.text = "${MainActivity.pm10}ug/m3"
        pollutant5_value.text = "${MainActivity.co}ug/m3"




        when (MainActivity.aqivalue) {
            1 -> {
                aqi_indextitle.text = "Healthy"
                aqdetail_seekbar.progressTintList = ColorStateList.valueOf(Color.GREEN)

                aqdetail_seekbar.setProgress(20)
                aq_detaildesc.setTextColor(ContextCompat.getColor(this, R.color.greencolor))
                aq_sensitivedesc.setTextColor(ContextCompat.getColor(this, R.color.greencolor))
                aq_detaildesc.text =
                    "Air quality is satisfactory, and air pollution poses less or no risk."

                aq_sensitivedesc.text =
                    " Air quality is satisfactory, and air pollution poses less or no risk."
            }

            2 -> {
                aqi_indextitle.text = "Fair"
                aqdetail_seekbar.setProgress(40)

                aqdetail_seekbar.progressTintList =
                    ColorStateList.valueOf(Color.parseColor("#FEAD16"))
                aq_detaildesc.setTextColor(ContextCompat.getColor(this, R.color.yellowcolor))
                aq_sensitivedesc.setTextColor(ContextCompat.getColor(this, R.color.yellowcolor))
                aq_detaildesc.text =
                    "Air quality is acceptable. However little risk for sensitive people."
                aq_sensitivedesc.text = "Mild risk for people sensitive to air pollution."
            }

            3 -> {
                aqi_indextitle.text = "Moderate"
                aqdetail_seekbar.setProgress(50)

                aqdetail_seekbar.progressTintList =
                    ColorStateList.valueOf(Color.parseColor("#F08510"))
                aq_detaildesc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                aq_sensitivedesc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                aq_detaildesc.text = "General public is less likely to be affected."
                aq_sensitivedesc.text = "Members of sensitive groups may experience health effects."
            }

            4 -> {
                aqi_indextitle.text = "Poor"
                aqdetail_seekbar.setProgress(70)
                aqdetail_seekbar.progressTintList =
                    ColorStateList.valueOf(Color.parseColor("#F08510"))
                aq_detaildesc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                aq_sensitivedesc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                aq_detaildesc.text = "Some members of general public may experience health effects."
                aq_sensitivedesc.text =
                    "Members of sensitive groups may experience more serious health effects."
            }

            5 -> {
                aqi_indextitle.text = "Very Poor"

                aqdetail_seekbar.progressTintList = ColorStateList.valueOf(Color.RED)

                //  aqdetail_seekbar.progressDrawable = ContextCompat.getDrawable(this,R.drawable.sliderc3)


                aqdetail_seekbar.setProgress(90)
                aq_detaildesc.setTextColor(ContextCompat.getColor(this, R.color.redcolor))
                aq_sensitivedesc.setTextColor(ContextCompat.getColor(this, R.color.redcolor))
                aq_detaildesc.text = "Risk of health effects is increased for everyone."
                aq_sensitivedesc.text = "Risk of health effects is increased for everyone."
            }
        }


        when (MainActivity.no2!!) {
            in 0.0..30.0 -> {
                pollutant1_desc.text = "Good"
                pollutant1_desc.setTextColor(ContextCompat.getColor(this, R.color.greencolor))
                img_pollutant1.text = "1"
                img_pollutant1.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant1)
            }
            in 41.0..80.0 -> {
                pollutant1_desc.text = "Satisfactory"
                pollutant1_desc.setTextColor(ContextCompat.getColor(this, R.color.yellowcolor))
                img_pollutant1.text = "2"
                img_pollutant1.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant3)
            }
            in 81.0..180.0 -> {
                pollutant1_desc.text = "Moderately Polluted"
                pollutant1_desc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                img_pollutant1.text = "3"
                img_pollutant1.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant2)
            }
            in 181.0..280.0 -> {
                pollutant1_desc.text = "Poor"
                pollutant1_desc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                img_pollutant1.text = "4"
                img_pollutant1.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant2)
            }
            in 281.0..400.0 -> {
                pollutant1_desc.text = "Very Poor"
                pollutant1_desc.setTextColor(ContextCompat.getColor(this, R.color.redcolor))
                img_pollutant1.text = "5"
                img_pollutant1.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutantred)
            }
            in 400.0..800.0 -> {
                pollutant1_desc.text = "Severe"
                pollutant1_desc.setTextColor(ContextCompat.getColor(this, R.color.redcolor))
                img_pollutant1.text = "5"
                img_pollutant1.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutantred)
            }
        }

        when (MainActivity.o3!!) {
            in 0.0..50.0 -> {
                pollutant2_desc.text = "Good"
                pollutant2_desc.setTextColor(ContextCompat.getColor(this, R.color.greencolor))
                img_pollutant2.text = "1"
                img_pollutant2.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant1)
            }
            in 51.0..100.0 -> {
                pollutant2_desc.text = "Satisfactory"
                pollutant2_desc.setTextColor(ContextCompat.getColor(this, R.color.yellowcolor))
                img_pollutant2.text = "2"
                img_pollutant2.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant3)
            }
            in 101.0..168.0 -> {
                pollutant2_desc.text = "Moderately Polluted"
                pollutant2_desc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                img_pollutant2.text = "3"
                img_pollutant2.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant2)
            }
            in 169.0..208.0 -> {
                pollutant2_desc.text = "Poor"
                pollutant2_desc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                img_pollutant2.text = "4"
                img_pollutant2.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant2)
            }
            in 209.0..748.0 -> {
                pollutant2_desc.text = "Very Poor"
                pollutant2_desc.setTextColor(ContextCompat.getColor(this, R.color.redcolor))
                img_pollutant2.text = "5"
                img_pollutant2.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutantred)
            }
            in 749.0..1000.0 -> {
                pollutant2_desc.text = "Severe"
                pollutant2_desc.setTextColor(ContextCompat.getColor(this, R.color.vpoorcolor))
                img_pollutant2.text = "5"
                img_pollutant2.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutantred)
            }
        }

        when (MainActivity.so2!!) {
            in 0.0..30.0 -> {
                pollutant3_desc.text = "Good"
                pollutant3_desc.setTextColor(ContextCompat.getColor(this, R.color.greencolor))
                img_pollutant3.text = "1"
                img_pollutant3.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant1)
            }
            in 41.0..80.0 -> {
                pollutant3_desc.text = "Satisfactory"
                pollutant3_desc.setTextColor(ContextCompat.getColor(this, R.color.yellowcolor))
                img_pollutant3.text = "2"
                img_pollutant3.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant3)
            }
            in 81.0..380.0 -> {
                pollutant3_desc.text = "Moderately Polluted"
                pollutant3_desc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                img_pollutant3.text = "3"
                img_pollutant3.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant2)
            }
            in 381.0..800.0 -> {
                pollutant3_desc.text = "Poor"
                pollutant3_desc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                img_pollutant3.text = "4"
                img_pollutant3.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant2)
            }
            in 801.0..1600.0 -> {
                pollutant3_desc.text = "Very Poor"
                pollutant3_desc.setTextColor(ContextCompat.getColor(this, R.color.redcolor))
                img_pollutant3.text = "5"
                img_pollutant3.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutantred)
            }
            in 1600.0..2000.0 -> {
                pollutant3_desc.text = "Severe"
                pollutant3_desc.setTextColor(ContextCompat.getColor(this, R.color.redcolor))
                img_pollutant3.text = "5"
                img_pollutant3.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutantred)
            }
        }

        when (MainActivity.pm10!!) {
            in 0.0..50.0 -> {
                pollutant4_desc.text = "Good"
                pollutant4_desc.setTextColor(ContextCompat.getColor(this, R.color.greencolor))
                img_pollutant4.text = "1"
                img_pollutant4.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant1)
            }
            in 51.0..100.0 -> {
                pollutant4_desc.text = "Satisfactory"
                pollutant4_desc.setTextColor(ContextCompat.getColor(this, R.color.yellowcolor))
                img_pollutant4.text = "2"
                img_pollutant4.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant3)
            }
            in 101.0..250.0 -> {
                pollutant4_desc.text = "Moderately Polluted"
                pollutant4_desc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                img_pollutant4.text = "3"
                img_pollutant4.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant2)
            }
            in 251.0..350.0 -> {
                pollutant4_desc.text = "Poor"
                pollutant4_desc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                img_pollutant4.text = "4"
                img_pollutant4.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant2)
            }
            in 351.0..430.0 -> {
                pollutant4_desc.text = "Very Poor"
                pollutant4_desc.setTextColor(ContextCompat.getColor(this, R.color.redcolor))
                img_pollutant4.text = "5"
                img_pollutant4.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutantred)
            }
            in 431.0..1000.0 -> {
                pollutant4_desc.text = "Severe"
                pollutant4_desc.setTextColor(ContextCompat.getColor(this, R.color.redcolor))
                img_pollutant4.text = "5"
                img_pollutant4.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutantred)
            }
        }

        when (MainActivity.co!!) {
            in 0.0..1.0 -> {
                pollutant5_desc.text = "Good"
                pollutant5_desc.setTextColor(ContextCompat.getColor(this, R.color.greencolor))
                img_pollutant5.text = "1"
                img_pollutant5.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant1)
            }
            in 1.1..2.0 -> {
                pollutant5_desc.text = "Satisfactory"
                pollutant5_desc.setTextColor(ContextCompat.getColor(this, R.color.yellowcolor))
                img_pollutant5.text = "2"
                img_pollutant5.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant3)
            }
            in 2.1..10.0 -> {
                pollutant5_desc.text = "Moderately Polluted"
                pollutant5_desc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                img_pollutant5.text = "3"
                img_pollutant5.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant2)
            }
            in 10.1..17.0 -> {
                pollutant5_desc.text = "Poor"
                pollutant5_desc.setTextColor(ContextCompat.getColor(this, R.color.moderatecolor))
                img_pollutant5.text = "4"
                img_pollutant5.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutant2)
            }
            in 17.1..34.0 -> {
                pollutant5_desc.text = "Very Poor"
                pollutant5_desc.setTextColor(ContextCompat.getColor(this, R.color.redcolor))
                img_pollutant5.text = "5"
                img_pollutant5.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutantred)
            }
            in 34.1..10000.0 -> {
                pollutant5_desc.text = "Severe"
                pollutant5_desc.setTextColor(ContextCompat.getColor(this, R.color.redcolor))
                img_pollutant5.text = "5"
                img_pollutant5.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_pollutantred)
            }
        }


        val btn_back = findViewById<ImageView>(R.id.back_btn)
        btn_back.setOnClickListener {


            onBackPressed()

        }


    }


}