package com.example.weatherapp2


import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.codemybrainsout.ratingdialog.RatingDialog
import com.example.weatherapp2.Dailydata.DailyDataAdapter
import com.example.weatherapp2.Database.*
import com.example.weatherapp2.Hourlydata.HourlyDataAdapter
import com.example.weatherapp2.SplashScreen.Companion.isloc_granted
import com.example.weatherapp2.SplashScreen.Companion.sharedpref
import com.example.weatherapp2.databinding.ActivityMain2Binding
import com.example.weatherapp2.repository.ApiRepository
import com.example.weatherapp2.viewmodel.APIViewModel
import com.example.weatherapp2.viewmodel.ApiViewModelFactory
import com.github.tianma8023.ssv.SunriseSunsetView
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var apiViewModel: APIViewModel
    lateinit var context: Context

    var db: WeatherDB? = null
    lateinit var binding: ActivityMain2Binding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var cm: ConnectivityManager
    private var networkinfo: NetworkInfo? = null


    lateinit var mainHandler: Handler
    var sharepreff: SharedPreferences? = null


    private var sunrisehours: Int = 0
    private var sunrisemin: Int = 0
    private var sunsethours: Int = 0
    private var sunsetmin: Int = 0


    var timeunit: String? = null
    lateinit var tempunit: String
    lateinit var windspeedunit: String
    lateinit var pressureunit: String


    lateinit var mSunriseSunsetView: SunriseSunsetView
    private lateinit var ad: TemplateView


    // to make variable static, companion object is used
    companion object {
        lateinit var hud: KProgressHUD
        var area: String? = null
        lateinit var dailydatalist: List<DailyTable>


        var icon_id: Int = 0
        var tmp_incelcius: Int = 0

        var aqivalue: Int? = 0
        var co: Double? = 0.0
        var o3: Double? = 0.0
        var no: Double? = 0.0
        var no2: Double? = 0.0
        var nh3: Double? = 0.0
        var so2: Double? = 0.0
        var pm2_5: Double? = 0.0
        var pm10: Double? = 0.0


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)



        intro()


        // loading native ad
        val adLoader = AdLoader.Builder(this, resources.getString(R.string.native_id))
            .forNativeAd { nativeAd ->

                binding.nativelayoutMain.visibility = View.VISIBLE
                ad.setNativeAd(nativeAd)
            }.build()

        adLoader.loadAd(AdRequest.Builder().build())


        sharedpref = getSharedPreferences("launchpref", MODE_PRIVATE)
        isloc_granted = sharedpref.getBoolean("islocgranted", false)

        val apiInterface = RetrofitHelper.getRetrofItinstance().create(ApiInterface::class.java)

        val apiRepository = ApiRepository(apiInterface, db!!)

        apiViewModel = ViewModelProvider(
            this,
            ApiViewModelFactory(apiRepository)
        ).get(APIViewModel::class.java)


        // gettig time and temp units
        timeunit = sharepreff!!.getString("timeunit", "12hr")!!
        tempunit = sharepreff!!.getString("tempunit", "celcius")!!




        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            Log.e("weatherapp", "onCreate: location granted true")
            isloc_granted = true
            sharedpref.edit().putBoolean("islocgranted", isloc_granted)
            Loc_PermissionGranted()


        } else {
            Log.e("weatherapp", "onCreate: location granted false")
            Log.e("weatherapp", "onCreate: requesting location access")
            // request for location permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                100
            )
        }


        askRatings()

        binding.swiperefreshLayout.setOnRefreshListener {


            val snackbar = Snackbar.make(
                binding.mainactivityLayout, "Loading Data",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            snackbar.show()
            Toast.makeText(this, "checking internet & gps", Toast.LENGTH_SHORT).show()
            Loc_PermissionGranted()


            binding.swiperefreshLayout.isRefreshing = false

        }

        // Configure the refreshing colors
        binding.swiperefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )


    }


    private fun intro() {


        context = this
        db = WeatherDB.getDB(this)




        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setDimAmount(0.5f)
        mSunriseSunsetView = findViewById(R.id.ssv) as SunriseSunsetView
        ad = findViewById(R.id.nativelayout_main)

        sharepreff = this.getSharedPreferences("setting_prefernces", MODE_PRIVATE)
        mainHandler = Handler(Looper.getMainLooper())

        cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        networkinfo = cm.activeNetworkInfo

        binding.airqualityLayout.setOnClickListener {


            if (networkinfo != null) {
                hud.setLabel("Loading AD...")
                hud.show()

                val adRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    this,
                    resources.getString(R.string.interstitial_id),
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialad: InterstitialAd) {
                            hud.dismiss()

                            interstitialad.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {


                                        startActivity(
                                            Intent(
                                                this@MainActivity,
                                                AQI_Activity::class.java
                                            )
                                        )

                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                        startActivity(
                                            Intent(
                                                this@MainActivity,
                                                AQI_Activity::class.java
                                            )
                                        )
                                    }

                                    override fun onAdShowedFullScreenContent() {


                                    }
                                }

                            interstitialad.show(this@MainActivity)

                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            hud.dismiss()
                            startActivity(Intent(this@MainActivity, AQI_Activity::class.java))
                        }
                    })
            } else {
                startActivity(
                    Intent(
                        this@MainActivity,
                        AQI_Activity::class.java
                    )
                )
            }


        }

        binding.dailyLayout.setOnClickListener {


            if (networkinfo != null) {
                hud.setLabel("Loading AD...")
                hud.show()

                val adRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    this,
                    resources.getString(R.string.interstitial_id),
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialad: InterstitialAd) {
                            hud.dismiss()

                            interstitialad.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {


                                        startActivity(
                                            Intent(
                                                this@MainActivity,
                                                DailyWeatherDetail_Activity::class.java
                                            )
                                        )

                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                        startActivity(
                                            Intent(
                                                this@MainActivity,
                                                DailyWeatherDetail_Activity::class.java
                                            )
                                        )
                                    }

                                    override fun onAdShowedFullScreenContent() {

                                    }
                                }

                            interstitialad.show(this@MainActivity)

                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            hud.dismiss()
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    DailyWeatherDetail_Activity::class.java
                                )
                            )
                        }
                    })
            } else {
                startActivity(Intent(this, DailyWeatherDetail_Activity::class.java))
            }


        }

        binding.btnMenu.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }


    }

    private fun Loc_PermissionGranted() {


        if (CheckInternet_GPS()) {
            Log.e("weatherapp", "onCreate: internet & gps is on")


            GetLatLng()

        } else {


            showCurrentData()
            showHourlyData(timeunit!!, tempunit)
            showDailyData(tempunit)
            showAQIData()


            val snackbar = Snackbar.make(
                binding.mainactivityLayout, "Internet or GPS is off",
                Snackbar.LENGTH_LONG
            )
            snackbar.show()

            Log.e("weatherapp", "onCreate: internet or gps is off")
        }


    }


    @SuppressLint("MissingPermission")
    private fun GetLatLng() {

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->


            if (location != null) {
                Log.e("weatherapp", "GetLatLng: first attempt lat is ${location.latitude}")



                area = getArea_frmlatlng(location.latitude, location.longitude)
                Log.e("weatherapp", "GetLatLng: area is ${area}")

                if (area != null) {


                    apiViewModel.SendAPIRequest(location.latitude, location.longitude, area!!)


                } else {
                    Log.e("weatherapp", "GetLatLng: area is not fetched")


                    apiViewModel.SendAPIRequest(
                        location.latitude,
                        location.longitude,
                        "area not fetched"
                    )

                }

                showCurrentData()
                showHourlyData(timeunit!!, tempunit)
                showDailyData(tempunit)
                showAQIData()

            }
            else {

                val locRequest = LocationRequest.create().apply {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = TimeUnit.SECONDS.toMillis(60)
                    fastestInterval = TimeUnit.SECONDS.toMillis(60)
                }

                fusedLocationProviderClient.requestLocationUpdates(
                    locRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)



                            Log.e(
                                "weatherapp",
                                "GetLatLng: 2nd attempt lat is ${locationResult.lastLocation.latitude}",
                            )
                            area = getArea_frmlatlng(
                                locationResult.lastLocation.latitude,
                                locationResult.lastLocation.longitude
                            )
                            Log.e("weatherapp", "GetLatLng: area is ${area}")


                            if (area != null) {
                                apiViewModel.SendAPIRequest(
                                    locationResult.lastLocation.latitude,
                                    locationResult.lastLocation.longitude, area!!
                                )


                            } else {
                                Log.e("weatherapp", "GetLatLng: area is not fetched")

                                apiViewModel.SendAPIRequest(
                                    locationResult.lastLocation.latitude,
                                    locationResult.lastLocation.longitude, "area not fetched"
                                )


                            }

                            showCurrentData()
                            showHourlyData(timeunit!!, tempunit)
                            showDailyData(tempunit)
                            showAQIData()


                        }
                    },
                    Looper.getMainLooper()
                )
            }
        }

    }


    private fun CheckInternet_GPS(): Boolean {

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        networkinfo = cm.activeNetworkInfo

        return networkinfo != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    fun askRatings() {
        val ratingDialog = RatingDialog.Builder(this)
            .threshold(3f)
            .session(2)
            .onRatingBarFormSumbit { feedback: String? -> }.build()
        ratingDialog.show()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.settingsicon_layout -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                binding.drawerLayout.closeDrawer(GravityCompat.START)

            }

            R.id.moreappsicon_layout -> {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/developer?id=paksol+video+downloader+%26+video+saver")
                        )
                    )
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/developer?id=Developer+Name+Here")
                        )
                    )
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START)

            }

            R.id.rateusicon_layout -> {
                val uri = Uri.parse("market://details?id=" + applicationContext.packageName)
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                goToMarket.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                        )
                    )
                }

                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.shareicon_layout -> {
                val appPackageName = applicationContext.packageName
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out the App at: https://play.google.com/store/apps/details?id=$appPackageName"
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.policyicon_layout -> {
                val privacylink = Uri.parse("http://paksolapps.epizy.com/?i=2")
                val privacyintent2 = Intent(Intent.ACTION_VIEW, privacylink)
                startActivity(privacyintent2)

                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

        }
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {

            super.onBackPressed()

            /*val exitDialog = ExitDialog(this@MainActivity, mNativeAd)
            exitDialog.show()
            val window: Window? = exitDialog.window
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )*/
        }

    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                isloc_granted = true
                sharedpref.edit().putBoolean("islocgranted", isloc_granted)
                Log.e(
                    "weatherapp",
                    "onrequestpermissionresult: location permission granted on request",
                )

                Loc_PermissionGranted()


            }
            // when location permission is denied
            else {

                isloc_granted = false
                sharedpref.edit().putBoolean("islocgranted", isloc_granted)
                Toast.makeText(this, "Location Permission is required", Toast.LENGTH_SHORT).show()
                Log.e(
                    "weatherapp",
                    "onrequestpermissionresult: location permission not granted on request",
                )
            }
        }
    }


    fun getArea_frmlatlng(lat: Double, lng: Double): String? {
        var areaa: String? = null
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresslist: List<Address?> = geocoder.getFromLocation(lat, lng, 1)

            if (addresslist.isNotEmpty()) {
                areaa = addresslist[0]!!.subLocality
            } else {
                areaa = "area not fetched"
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return areaa

    }


    private fun showAQIData() {

        if (db!!.weatherDao().getAQIDatafrmDB().isNotEmpty()) {
            val aqiList = db!!.weatherDao().getAQIDatafrmDB()
            binding.tvAqindex.text = aqiList[0].aqi.toString()

            aqivalue = aqiList[0].aqi
            co = aqiList[0].co
            o3 = aqiList[0].o3
            no = aqiList[0].no
            no2 = aqiList[0].no2
            nh3 = aqiList[0].nh3
            so2 = aqiList[0].so2
            pm2_5 = aqiList[0].pm2_5
            pm10 = aqiList[0].pm10


            when (aqivalue) {
                1 -> {
                    binding.tvAqDesc.setText("Healthy")
                    binding.aqProgressbar.setProgress(20)
                    binding.aqProgressbar.progressTintList =
                        ColorStateList.valueOf(Color.GREEN)


                }
                2 -> {
                    binding.tvAqDesc.setText("Fair")
                    binding.aqProgressbar.setProgress(40)
                    binding.aqProgressbar.progressTintList =
                        ColorStateList.valueOf(Color.parseColor("#FEAD16"))

                }
                3 -> {
                    binding.tvAqDesc.setText("Moderate")
                    binding.aqProgressbar.setProgress(50)
                    binding.aqProgressbar.progressTintList =
                        ColorStateList.valueOf(Color.parseColor("#F08510"))
                }
                4 -> {
                    binding.tvAqDesc.setText("Poor")
                    binding.aqProgressbar.setProgress(70)
                    binding.aqProgressbar.progressTintList =
                        ColorStateList.valueOf(Color.parseColor("#F08510"))
                }
                5 -> {
                    binding.tvAqDesc.setText("Very Poor")
                    binding.aqProgressbar.setProgress(90)
                    binding.aqProgressbar.progressTintList =
                        ColorStateList.valueOf(Color.RED)
                }
            }


        }




        /*apiViewModel.Showaqidata().observe(this, {

            if (it != null) {
                binding.tvAqindex.text = it[0].aqi.toString()

                aqivalue = it[0].aqi
                co = it[0].co
                o3 = it[0].o3
                no = it[0].no
                no2 = it[0].no2
                nh3 = it[0].nh3
                so2 = it[0].so2
                pm2_5 = it[0].pm2_5
                pm10 = it[0].pm10


                when (aqivalue) {
                    1 -> {
                        binding.tvAqDesc.setText("Healthy")
                        binding.aqProgressbar.setProgress(20)
                        binding.aqProgressbar.progressTintList =
                            ColorStateList.valueOf(Color.GREEN)


                    }
                    2 -> {
                        binding.tvAqDesc.setText("Fair")
                        binding.aqProgressbar.setProgress(40)
                        binding.aqProgressbar.progressTintList =
                            ColorStateList.valueOf(Color.parseColor("#FEAD16"))

                    }
                    3 -> {
                        binding.tvAqDesc.setText("Moderate")
                        binding.aqProgressbar.setProgress(50)
                        binding.aqProgressbar.progressTintList =
                            ColorStateList.valueOf(Color.parseColor("#F08510"))
                    }
                    4 -> {
                        binding.tvAqDesc.setText("Poor")
                        binding.aqProgressbar.setProgress(70)
                        binding.aqProgressbar.progressTintList =
                            ColorStateList.valueOf(Color.parseColor("#F08510"))
                    }
                    5 -> {
                        binding.tvAqDesc.setText("Very Poor")
                        binding.aqProgressbar.setProgress(90)
                        binding.aqProgressbar.progressTintList =
                            ColorStateList.valueOf(Color.RED)
                    }
                }
            } else {
                Toast.makeText(this, "NO data fetched, pull to refresh", Toast.LENGTH_SHORT).show()
            }


        })*/


    }


    fun showCurrentData() {

if (db!!.weatherDao().getCurrentDatafrmDB().isNotEmpty()){

    val currentweather_dblist = db!!.weatherDao().getCurrentDatafrmDB()

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


    binding.progressbarLayout.visibility = View.GONE
    // handler to update current temperature,time,wind and pressure units and setting values on ui
    mainHandler.postDelayed(object : Runnable {
        override fun run() {

            // current time, temp, wind and pressure units
            timeunit = sharepreff!!.getString("timeunit", "12hr")!!
            tempunit = sharepreff!!.getString("tempunit", "celcius")!!
            windspeedunit = sharepreff!!.getString("windspeedunit", "km")!!
            pressureunit = sharepreff!!.getString("pressureunit", "mbar")!!

            // setting time according to  selected time format
            if (timeunit.equals("12hr")) {

                val formatted: String = SimpleDateFormat(
                    "  dd,MMM  hh:mm:ss a",
                    Locale.getDefault()
                )
                    .format(Date())
                binding.tvDatetime.text = formatted
            } else if (timeunit.equals("24hr")) {

                val formatted: String = SimpleDateFormat(
                    "  dd,MMM  HH:mm:ss a",
                    Locale.getDefault()
                ).format(Date())
                binding.tvDatetime.text = formatted
            }

            // setting temperature according to  selected temperature unit
            if (tempunit.equals("celcius")) {
                binding.tvCurrTemp.text = String.format(
                    Locale.getDefault(),
                    "%s°c",
                    "  ${((currentWeatherTable.temp - 273.15).roundToInt())}"
                )
            } else if (tempunit.equals("farenheit")) {

                binding.tvCurrTemp.text =
                    String.format(
                        Locale.getDefault(),
                        "%s°f",
                        "  ${((tmp_incelcius * 1.8) + 32).toInt()}"
                    )

            }


            // setting wind according to  selected wind unit
            if (windspeedunit.equals("km")) {

                binding.tvWindSpeed.text = currentWeatherTable.wind_speed.toString()
                binding.tvWindspeedValue.text = currentWeatherTable.wind_speed.toString()
            } else {
                binding.tvWindSpeed.text = (currentWeatherTable.wind_speed * 1000).toString()

                binding.tvWindspeedValue.text =
                    (currentWeatherTable.wind_speed * 1000).toString()
                binding.tvWindspeedunit.text = "m/h"
            }

            // setting pressure according to  selected pressure unit
            if (pressureunit.equals("mbar")) {
                binding.tvPressureValue.text = currentWeatherTable.pressure.toString()
                binding.tvBarometerUnits.text = "mBar"
                binding.tvBarometerValue.text = currentWeatherTable.pressure.toString()

            } else if (pressureunit.equals("inhg")) {
                binding.tvPressureValue.text =
                    ((currentWeatherTable.pressure * 0.02953).toInt()).toString()
                binding.tvBarometerUnits.text = "inHg"
                binding.tvBarometerValue.text =
                    ((currentWeatherTable.pressure * 0.02953).toInt()).toString()
            } else if (pressureunit.equals("kpa")) {
                binding.tvPressureValue.text =
                    ((currentWeatherTable.pressure / 10).toInt()).toString()
                binding.tvBarometerUnits.text = "Kpa"
                binding.tvBarometerValue.text =
                    ((currentWeatherTable.pressure / 10).toInt()).toString()

            } else if (pressureunit.equals("mmhg")) {
                binding.tvPressureValue.text =
                    ((currentWeatherTable.pressure / 1.333).toInt()).toString()
                binding.tvBarometerUnits.text = "mmHg"
                binding.tvBarometerValue.text =
                    ((currentWeatherTable.pressure / 1.01325).toInt()).toString()
            }


            mainHandler.postDelayed(this, 1000)
        }
    }, 1000)


    /////////////////// displaying current data on ui  ////////////////////////////////////


    binding.tvArea.text = currentWeatherTable.area

    // rain chance
    binding.tvRainChance.text = currentWeatherTable.clouds.toString()
    binding.tvRainChance.setCompoundDrawablesWithIntrinsicBounds(
        R.drawable.ic_rainchances,
        0,
        0,
        0,
    )
    // humidity percentage
    binding.tvHumidity.text = currentWeatherTable.humidity.toString()

    binding.tvHumidity.setCompoundDrawablesWithIntrinsicBounds(
        R.drawable.ic_humidity,
        0,
        0,
        0,
    )

    // drawable for wind speed
    binding.tvWindSpeed.setCompoundDrawablesWithIntrinsicBounds(
        R.drawable.ic_wind,
        0,
        0,
        0
    )

    // current weather icon
    if (Build.VERSION.SDK_INT > 23) {
        binding.imgWeatherIcon.load("https://openweathermap.org/img/w/${currentWeatherTable.weathericon}.png")
    } else {
        binding.imgWeatherIcon.load("http://openweathermap.org/img/w/${currentWeatherTable.weathericon}.png")
    }


    //binding.tvLocation.text = crntweatherData.area
    //current weather description
    binding.tvWeatherDesc.text = currentWeatherTable.weatherdesc


    // current feels like, humidity, visibility, uv_index,dew_point
    binding.tvFeelsValues.text =
        (((currentWeatherTable.feels_like) - 273.15).roundToInt()).toString()
    binding.tvHumidityValue.text = currentWeatherTable.humidity.toString()
    binding.tvUvindexValue.text = currentWeatherTable.uvi.toString()
    binding.tvVisibilityValue.text = (currentWeatherTable.visibility / 1000).toString() + "Km"
    binding.tvDewValue.text = ((currentWeatherTable.dew_point - 273.15).roundToInt()).toString()


    // changing background according to weather condition
    icon_id = currentWeatherTable.currenticonid
    when (icon_id) {
        in 200..232 -> {
            binding.mainactivityLayout.background = ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.thunderstormbg
            )


        }
        in 300..321 -> {

            binding.mainactivityLayout.background =
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.windybg
                )

        }
        in 500..531 -> {
            binding.mainactivityLayout.background =
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.rainybg
                )

        }
        in 600..622 -> {
            binding.mainactivityLayout.background =
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.snowybg
                )

        }
        in 701..781 -> {
            binding.mainactivityLayout.background =
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.cloudybg
                )

        }
        800 -> {
            binding.mainactivityLayout.background =
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.sunnybg
                )


        }
        in 801..804 -> {
            binding.mainactivityLayout.background =
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.cloudybg
                )


        }
        else -> {
            binding.mainactivityLayout.background =
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.sunnybg
                )

        }
    }


    calculateDayLength(currentWeatherTable)









}


else {
    binding.dataloadingProgressbar.visibility = View.GONE
    binding.progressbarLayout.visibility = View.GONE
    binding.tvLoadingdata.text = "Data Not Fetched, Swipe from top to refresh"
    Toast.makeText(this, "data not fetched", Toast.LENGTH_SHORT).show()
}


       /* apiViewModel.crntlivedata.observe(this, {

            if (it != null) {

                binding.progressbarLayout.visibility = View.GONE
                // handler to update current temperature,time,wind and pressure units and setting values on ui
                mainHandler.postDelayed(object : Runnable {
                    override fun run() {

                        // current time, temp, wind and pressure units
                        timeunit = sharepreff!!.getString("timeunit", "12hr")!!
                        tempunit = sharepreff!!.getString("tempunit", "celcius")!!
                        windspeedunit = sharepreff!!.getString("windspeedunit", "km")!!
                        pressureunit = sharepreff!!.getString("pressureunit", "mbar")!!

                        // setting time according to  selected time format
                        if (timeunit.equals("12hr")) {

                            val formatted: String = SimpleDateFormat(
                                "  dd,MMM  hh:mm:ss a",
                                Locale.getDefault()
                            )
                                .format(Date())
                            binding.tvDatetime.text = formatted
                        } else if (timeunit.equals("24hr")) {

                            val formatted: String = SimpleDateFormat(
                                "  dd,MMM  HH:mm:ss a",
                                Locale.getDefault()
                            ).format(Date())
                            binding.tvDatetime.text = formatted
                        }

                        // setting temperature according to  selected temperature unit
                        if (tempunit.equals("celcius")) {
                            binding.tvCurrTemp.text = String.format(
                                Locale.getDefault(),
                                "%s°c",
                                "  ${((it.temp - 273.15).roundToInt())}"
                            )
                        } else if (tempunit.equals("farenheit")) {

                            binding.tvCurrTemp.text =
                                String.format(
                                    Locale.getDefault(),
                                    "%s°f",
                                    "  ${((tmp_incelcius * 1.8) + 32).toInt()}"
                                )

                        }


                        // setting wind according to  selected wind unit
                        if (windspeedunit.equals("km")) {

                            binding.tvWindSpeed.text = it.wind_speed.toString()
                            binding.tvWindspeedValue.text = it.wind_speed.toString()
                        } else {
                            binding.tvWindSpeed.text = (it.wind_speed * 1000).toString()

                            binding.tvWindspeedValue.text =
                                (it.wind_speed * 1000).toString()
                            binding.tvWindspeedunit.text = "m/h"
                        }

                        // setting pressure according to  selected pressure unit
                        if (pressureunit.equals("mbar")) {
                            binding.tvPressureValue.text = it.pressure.toString()
                            binding.tvBarometerUnits.text = "mBar"
                            binding.tvBarometerValue.text = it.pressure.toString()

                        } else if (pressureunit.equals("inhg")) {
                            binding.tvPressureValue.text =
                                ((it.pressure * 0.02953).toInt()).toString()
                            binding.tvBarometerUnits.text = "inHg"
                            binding.tvBarometerValue.text =
                                ((it.pressure * 0.02953).toInt()).toString()
                        } else if (pressureunit.equals("kpa")) {
                            binding.tvPressureValue.text =
                                ((it.pressure / 10).toInt()).toString()
                            binding.tvBarometerUnits.text = "Kpa"
                            binding.tvBarometerValue.text =
                                ((it.pressure / 10).toInt()).toString()

                        } else if (pressureunit.equals("mmhg")) {
                            binding.tvPressureValue.text =
                                ((it.pressure / 1.333).toInt()).toString()
                            binding.tvBarometerUnits.text = "mmHg"
                            binding.tvBarometerValue.text =
                                ((it.pressure / 1.01325).toInt()).toString()
                        }


                        mainHandler.postDelayed(this, 1000)
                    }
                }, 1000)


                /////////////////// displaying current data on ui  ////////////////////////////////////


                binding.tvArea.text = it.area

                // rain chance
                binding.tvRainChance.text = it.clouds.toString()
                binding.tvRainChance.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_rainchances,
                    0,
                    0,
                    0,
                )
                // humidity percentage
                binding.tvHumidity.text = it.humidity.toString()

                binding.tvHumidity.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_humidity,
                    0,
                    0,
                    0,
                )

                // drawable for wind speed
                binding.tvWindSpeed.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_wind,
                    0,
                    0,
                    0
                )

                // current weather icon
                if (Build.VERSION.SDK_INT > 23) {
                    binding.imgWeatherIcon.load("https://openweathermap.org/img/w/${it.weathericon}.png")
                } else {
                    binding.imgWeatherIcon.load("http://openweathermap.org/img/w/${it.weathericon}.png")
                }


                //binding.tvLocation.text = crntweatherData.area
                //current weather description
                binding.tvWeatherDesc.text = it.weatherdesc


                // current feels like, humidity, visibility, uv_index,dew_point
                binding.tvFeelsValues.text =
                    (((it.feels_like) - 273.15).roundToInt()).toString()
                binding.tvHumidityValue.text = it.humidity.toString()
                binding.tvUvindexValue.text = it.uvi.toString()
                binding.tvVisibilityValue.text = (it.visibility / 1000).toString() + "Km"
                binding.tvDewValue.text = ((it.dew_point - 273.15).roundToInt()).toString()


                // changing background according to weather condition
                icon_id = it.currenticonid
                when (icon_id) {
                    in 200..232 -> {
                        binding.mainactivityLayout.background = ContextCompat.getDrawable(
                            this@MainActivity,
                            R.drawable.thunderstormbg
                        )


                    }
                    in 300..321 -> {

                        binding.mainactivityLayout.background =
                            ContextCompat.getDrawable(
                                this@MainActivity,
                                R.drawable.windybg
                            )

                    }
                    in 500..531 -> {
                        binding.mainactivityLayout.background =
                            ContextCompat.getDrawable(
                                this@MainActivity,
                                R.drawable.rainybg
                            )

                    }
                    in 600..622 -> {
                        binding.mainactivityLayout.background =
                            ContextCompat.getDrawable(
                                this@MainActivity,
                                R.drawable.snowybg
                            )

                    }
                    in 701..781 -> {
                        binding.mainactivityLayout.background =
                            ContextCompat.getDrawable(
                                this@MainActivity,
                                R.drawable.cloudybg
                            )

                    }
                    800 -> {
                        binding.mainactivityLayout.background =
                            ContextCompat.getDrawable(
                                this@MainActivity,
                                R.drawable.sunnybg
                            )


                    }
                    in 801..804 -> {
                        binding.mainactivityLayout.background =
                            ContextCompat.getDrawable(
                                this@MainActivity,
                                R.drawable.cloudybg
                            )


                    }
                    else -> {
                        binding.mainactivityLayout.background =
                            ContextCompat.getDrawable(
                                this@MainActivity,
                                R.drawable.sunnybg
                            )

                    }
                }


                calculateDayLength(it)
            } else {
                binding.dataloadingProgressbar.visibility = View.GONE
                binding.tvLoadingdata.text = "Data Not Fetched, Swipe from top to refresh"
                Toast.makeText(this, "data not fetched", Toast.LENGTH_SHORT).show()
            }
        })*/


    }


    fun showHourlyData(time_unit: String, temp_unit: String) {


        if (db!!.weatherDao().getHourlyDatafrmDB().isNotEmpty()) {
            val hourlylist = db!!.weatherDao().getHourlyDatafrmDB()

            binding.hourlyrecview.adapter = HourlyDataAdapter(
                this,
                hourlylist,
                time_unit,
                temp_unit
            )

        }







    }

    fun showDailyData(temp_unit: String) {

        if (db!!.weatherDao().getDailyDatafrmDB().isNotEmpty()) {
            val dailylist = db!!.weatherDao().getDailyDatafrmDB()

            binding.dailyrecview.adapter = DailyDataAdapter(
                this,
                dailylist,
              0,
                temp_unit
            )

        }

    }

    fun calculateDayLength(currentWeatherModelClassWeatherData: CurrentWeatherTable) {

        // current day sunrise and sunset time
        val temp_sunrisetime = currentWeatherModelClassWeatherData.currentsunrise * 1000
        val temp_sunsettime = currentWeatherModelClassWeatherData.currentsunset * 1000

        // sunrise hour
        val sunrisedatehour = Date(temp_sunrisetime)
        val formathour: DateFormat =
            SimpleDateFormat("hh", Locale.getDefault())
        val tempsunrisehours: String = formathour.format(sunrisedatehour)
        sunrisehours = Integer.parseInt(tempsunrisehours)

        //sunrise minute
        val sunrisedatemin = Date(temp_sunrisetime)
        val formatmin: DateFormat =
            SimpleDateFormat("mm", Locale.getDefault())
        val tempsunrisemin: String = formatmin.format(sunrisedatemin)
        sunrisemin = Integer.parseInt(tempsunrisemin)

        //sunset hour
        val sunsetdatehour = Date(temp_sunsettime)
        val sunsetformathour: DateFormat =
            SimpleDateFormat("HH", Locale.getDefault())
        val tempsunsethours: String = sunsetformathour.format(sunsetdatehour)
        sunsethours = Integer.parseInt(tempsunsethours)

        //sunset minute
        val sunsetdatemin = Date(temp_sunsettime)
        val sunsetformatmin: DateFormat =
            SimpleDateFormat("mm", Locale.getDefault())
        val tempsunsetmin: String = sunsetformatmin.format(sunsetdatemin)
        sunsetmin = Integer.parseInt(tempsunsetmin)


        // sun set and sun rise  animation
        mSunriseSunsetView.sunriseTime = com.github.tianma8023.model.Time(sunrisehours, sunrisemin)
        mSunriseSunsetView.sunsetTime = com.github.tianma8023.model.Time(sunsethours, sunsetmin)
        mSunriseSunsetView.startAnimate()

        if ((sunsetmin - sunrisemin) > 0) {
            binding.tvSunrisetime.setText(
                "Length of the day:" + (sunsethours - sunrisehours) + "hr " + (sunsetmin - sunrisemin) + "min "
            )
        } else {
            binding.tvSunrisetime.setText(
                "Length of the day:${sunsethours - sunrisehours}hr 0 min "
            )
        }

    }


}