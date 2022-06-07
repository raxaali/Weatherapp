package com.example.weatherapp2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherapp2.SplashScreen.Companion.isloc_granted
import com.example.weatherapp2.SplashScreen.Companion.sharedpref
import com.example.weatherapp2.databinding.ActivityLocationpermissionBinding
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD


class Locationpermission_activity : AppCompatActivity() {
    private lateinit var binding: ActivityLocationpermissionBinding

    private lateinit var cm: ConnectivityManager
    private var networkinfo: NetworkInfo? = null

    private lateinit var locationManager: LocationManager

    lateinit var hud: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationpermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        networkinfo = cm.activeNetworkInfo
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)


        sharedpref = getSharedPreferences("launchpref", MODE_PRIVATE)
        isloc_granted = sharedpref.getBoolean("islocgranted", false)



        binding.btnlocdescAllow.setOnClickListener {


            ActivityCompat.requestPermissions(
                this@Locationpermission_activity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                10
            )

        }

        binding.tvlocdescDontAllow.setOnClickListener {

            isloc_granted = false
            sharedpref.edit().putBoolean("islocgranted", isloc_granted).apply()

            Toast.makeText(this, "Location Permission is required", Toast.LENGTH_SHORT).show()
        }

        val privacytxtclick = findViewById<TextView>(R.id.tvprivacylink)

        privacytxtclick.setOnClickListener {
            val privacylink: Uri?
            if (Build.VERSION.SDK_INT > 23) {
                privacylink = Uri.parse("https://paksolapps.epizy.com/?i=2")
            } else {
                privacylink = Uri.parse("http://paksolapps.epizy.com/?i=2")
            }

            val privacyintent2 = Intent(Intent.ACTION_VIEW, privacylink)
            startActivity(privacyintent2)
        }


    }

    fun movetonextactivity() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val mainIntent = Intent(
                this@Locationpermission_activity,
                MainActivity::class.java
            )
            startActivity(mainIntent)
            finish()

        } else {
            val snackbar =
                Snackbar.make(binding.root, "Turn On GPS", Snackbar.LENGTH_LONG)
                    .setAction("Settings", View.OnClickListener {
                        startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    })
            snackbar.show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 10) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                isloc_granted = true
                sharedpref.edit().putBoolean("islocgranted", isloc_granted).apply()

                movetonextactivity()


            }
            // when location permission is denied
            else {
                isloc_granted = false
                sharedpref.edit().putBoolean("islocgranted", isloc_granted).apply()


                Toast.makeText(this, "Location Permission is required", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && isloc_granted) {
            val mainIntent = Intent(
                this@Locationpermission_activity,
                MainActivity::class.java
            )
            startActivity(mainIntent)
            finish()
        }

    }
}