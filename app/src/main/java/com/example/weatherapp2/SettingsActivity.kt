package com.example.weatherapp2

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp2.databinding.ActivitySettingsBinding
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest

class SettingsActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var sharedpref: SharedPreferences
    private lateinit var time_switch: Switch

    lateinit var  binding_settings:ActivitySettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_settings = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding_settings.root)

        time_switch = findViewById(R.id.timeformat_switch)
        sharedpref = this.getSharedPreferences("setting_prefernces", MODE_PRIVATE)


        // loading native ad
        val adLoader = AdLoader.Builder(this, resources.getString(R.string.native_id))
            .forNativeAd { nativeAd ->

                binding_settings.nativeTemplateSettings.setNativeAd(nativeAd)

            }.build()

        adLoader.loadAd(AdRequest.Builder().build())

        // setting the temprature unit
        val tempunit = sharedpref.getString("tempunit", "celcius")
        if (tempunit.equals("celcius")) {
            binding_settings.tvCelciusSettings.setBackgroundResource(R.drawable.settingsbtn_bg)
            binding_settings.tvFarenheitSettings.setBackgroundResource(R.color.transparentcolor)
        } else {
            binding_settings.tvFarenheitSettings.setBackgroundResource(R.drawable.settingsbtn_bg)
            binding_settings.tvCelciusSettings.setBackgroundResource(R.color.transparentcolor)
        }


        // setting the value of switch for time format
        val timeformat = sharedpref.getString("timeunit", "12hr")
        if (timeformat.equals("12hr")) {
            time_switch.isChecked = false
        } else if (timeformat.equals("24hr")) {
            time_switch.isChecked = true
        }

        // setting wind speed unit
        val windspeedunit = sharedpref.getString("windspeedunit", "km")
        if (windspeedunit.equals("km")) {
            binding_settings.tvWindunitKm.setBackgroundResource(R.drawable.settingsbtn_bg)
            binding_settings.tvWindunitMph.setBackgroundResource(R.color.transparentcolor)
        } else {
            binding_settings.tvWindunitMph.setBackgroundResource(R.drawable.settingsbtn_bg)
             binding_settings.tvWindunitKm.setBackgroundResource(R.color.transparentcolor)
        }


        // setting pressure units
        val pressureunit = sharedpref.getString("pressureunit", "mbar")
        if (pressureunit.equals("mbar")) {
            binding_settings.tvPressureunitMBar.setBackgroundResource(R.drawable.settingsbtn_bg)
             binding_settings.tvPressureunitInhg.setBackgroundResource(R.color.transparentcolor)
             binding_settings.tvPressureunitKpa.setBackgroundResource(R.color.transparentcolor)
            binding_settings.tvPressureunitMmhg.setBackgroundResource(R.color.transparentcolor)
        } else if (pressureunit.equals("inhg")) {
             binding_settings.tvPressureunitInhg.setBackgroundResource(R.drawable.settingsbtn_bg)
             binding_settings.tvPressureunitMBar.setBackgroundResource(R.color.transparentcolor)
             binding_settings.tvPressureunitKpa.setBackgroundResource(R.color.transparentcolor)
            binding_settings.tvPressureunitMmhg.setBackgroundResource(R.color.transparentcolor)
        } else if (pressureunit.equals("kpa")) {
             binding_settings.tvPressureunitKpa.setBackgroundResource(R.drawable.settingsbtn_bg)
             binding_settings.tvPressureunitInhg.setBackgroundResource(R.color.transparentcolor)
             binding_settings.tvPressureunitMBar.setBackgroundResource(R.color.transparentcolor)
            binding_settings.tvPressureunitMmhg.setBackgroundResource(R.color.transparentcolor)
        } else if (pressureunit.equals("mmhg")) {
            binding_settings.tvPressureunitMmhg.setBackgroundResource(R.drawable.settingsbtn_bg)
             binding_settings.tvPressureunitInhg.setBackgroundResource(R.color.transparentcolor)
             binding_settings.tvPressureunitKpa.setBackgroundResource(R.color.transparentcolor)
             binding_settings.tvPressureunitMBar.setBackgroundResource(R.color.transparentcolor)

        }


    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.tv_celcius_settings -> {
                binding_settings.tvCelciusSettings.setBackgroundResource(R.drawable.settingsbtn_bg)
                binding_settings.tvFarenheitSettings.setBackgroundResource(R.color.transparentcolor)
                sharedpref.edit().putString("tempunit", "celcius").apply()
            }

            R.id.tv_farenheit_settings -> {
                binding_settings.tvFarenheitSettings.setBackgroundResource(R.drawable.settingsbtn_bg)
                binding_settings.tvCelciusSettings.setBackgroundResource(R.color.transparentcolor)
                sharedpref.edit().putString("tempunit", "farenheit").apply()
            }

            R.id.timeformat_switch -> {

                if (time_switch.isChecked) {
                    sharedpref.edit().putString("timeunit", "24hr").apply()
                    Toast.makeText(this, "24 hr format", Toast.LENGTH_SHORT).show()
                } else {
                    sharedpref.edit().putString("timeunit", "12hr").apply()
                    Toast.makeText(this, "12 hr format", Toast.LENGTH_SHORT).show()
                }
            }

            R.id. tv_windunit_km -> {
                 binding_settings.tvWindunitKm.setBackgroundResource(R.drawable.settingsbtn_bg)
                binding_settings.tvWindunitMph.setBackgroundResource(R.color.transparentcolor)
                sharedpref.edit().putString("windspeedunit", "km").apply()
            }

            R.id.tv_windunit_mph -> {
                binding_settings.tvWindunitMph.setBackgroundResource(R.drawable.settingsbtn_bg)
                 binding_settings.tvWindunitKm.setBackgroundResource(R.color.transparentcolor)
                sharedpref.edit().putString("windspeedunit", "mph").apply()
            }

            R.id. tv_pressureunit_mBar -> {
                sharedpref.edit().putString("pressureunit", "mbar").apply()

                 binding_settings.tvPressureunitMBar.setBackgroundResource(R.drawable.settingsbtn_bg)
                 binding_settings.tvPressureunitInhg.setBackgroundResource(R.color.transparentcolor)
                 binding_settings.tvPressureunitKpa.setBackgroundResource(R.color.transparentcolor)
                binding_settings.tvPressureunitMmhg.setBackgroundResource(R.color.transparentcolor)
            }

            R.id. tv_pressureunit_inhg -> {
                sharedpref.edit().putString("pressureunit", "inhg").apply()

                 binding_settings.tvPressureunitInhg.setBackgroundResource(R.drawable.settingsbtn_bg)
                 binding_settings.tvPressureunitMBar.setBackgroundResource(R.color.transparentcolor)
                 binding_settings.tvPressureunitKpa.setBackgroundResource(R.color.transparentcolor)
                binding_settings.tvPressureunitMmhg.setBackgroundResource(R.color.transparentcolor)
            }

            R.id. tv_pressureunit_kpa -> {
                sharedpref.edit().putString("pressureunit", "kpa").apply()

                 binding_settings.tvPressureunitKpa.setBackgroundResource(R.drawable.settingsbtn_bg)
              binding_settings.tvPressureunitInhg.setBackgroundResource(R.color.transparentcolor)
                 binding_settings.tvPressureunitMBar.setBackgroundResource(R.color.transparentcolor)
                binding_settings.tvPressureunitMmhg.setBackgroundResource(R.color.transparentcolor)
            }

            R.id.tv_pressureunit_mmhg -> {
                sharedpref.edit().putString("pressureunit", "mmhg").apply()

                binding_settings.tvPressureunitMmhg.setBackgroundResource(R.drawable.settingsbtn_bg)
                binding_settings.tvPressureunitInhg.setBackgroundResource(R.color.transparentcolor)
                binding_settings.tvPressureunitKpa.setBackgroundResource(R.color.transparentcolor)
                 binding_settings.tvPressureunitMBar.setBackgroundResource(R.color.transparentcolor)

            }

            R.id.img_backarrow_settings ->{
                onBackPressed()
            }


        }
    }
}