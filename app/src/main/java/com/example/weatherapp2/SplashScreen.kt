package com.example.weatherapp2

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp2.databinding.ActivitySplashScreenBinding
import com.google.android.material.snackbar.Snackbar
import java.util.zip.Inflater


lateinit var binding:ActivitySplashScreenBinding
class SplashScreen : AppCompatActivity() {
private lateinit var locationManager:LocationManager
    companion object {


        var isloc_granted = false

        lateinit var sharedpref: SharedPreferences

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)



        sharedpref = getSharedPreferences("launchpref", MODE_PRIVATE)
        isloc_granted = sharedpref.getBoolean("islocgranted",false)




        binding.splashAnimview.addAnimatorListener(object : AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                movetonextactivity()

            }

            override fun onAnimationCancel(p0: Animator?) {
                movetonextactivity()
            }

            override fun onAnimationRepeat(p0: Animator?) {

            }

        })

    }

    fun movetonextactivity(){

        if (isloc_granted) {
             locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                sharedpref.edit().putBoolean("islocgranted", isloc_granted).apply()

                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()

            }else{
                val snackbar =
                    Snackbar.make(binding.root, "Turn On GPS", Snackbar.LENGTH_LONG)
                        .setAction("Settings", View.OnClickListener {
                            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        })
                snackbar.show()
            }



        } else {

            isloc_granted = false
            sharedpref.edit().putBoolean("islocgranted", isloc_granted).apply()

            startActivity(Intent(this@SplashScreen, Locationpermission_activity::class.java))
            finish()
        }
    }

/*    override fun onResume() {
        super.onResume()

        isloc_granted = sharedpref.getBoolean("islocgranted ",false)
        var locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && isloc_granted){
            val mainIntent = Intent(
                this,
                MainActivity::class.java
            )
            startActivity(mainIntent)
            finish()
        }
    }*/
}