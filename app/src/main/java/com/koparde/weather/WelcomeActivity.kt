package com.koparde.weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.koparde.weather.databinding.ActivityWelcomeBinding
import java.util.*

class WelcomeActivity : AppCompatActivity() {
    lateinit var splashBinding : ActivityWelcomeBinding
    lateinit var fusedlocation : FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = splashBinding.root
        setContentView(view)

        fusedlocation = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()
    }


    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (CheckPermission()){
            if (LocationEnable()){
                fusedlocation.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if(location == null){

                        beginLocationRequest()
                    }else{
                         val handler = Handler(Looper.getMainLooper())
                         handler.postDelayed(object : Runnable{
                                override fun run() {
                                    val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                                    intent.putExtra("lat", location.latitude.toString())
                                    intent.putExtra("long", location.longitude.toString())
                                    startActivity(intent)
                                    finish()
                                }
                        },3000)
                    }
                }
            }else{
                Toast.makeText(this,"Please turn on your GPS ",Toast.LENGTH_LONG).show()
            }
        }else{
           RequestPermission()
        }
    }



    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    private fun beginLocationRequest() {
        var locationRequest = LocationRequest.Builder(0)
            .setQuality(LocationRequest.QUALITY_HIGH_ACCURACY)
            .setIntervalMillis(0L)
            .setMinUpdateIntervalMillis(0L)
            .setMaxUpdates(1)
            .build()
        fusedlocation = LocationServices.getFusedLocationProviderClient(this)
        fusedlocation.requestLocationUpdates(locationCallback, Looper.myLooper())

    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation: Location? = p0.lastLocation
       }
    }

    private fun LocationEnable(): Boolean {
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun RequestPermission() {

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
    }

    private fun CheckPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false

    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) { super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode==100 && grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            getLastLocation()
        }
    }


}

private fun FusedLocationProviderClient.requestLocationUpdates(locationCallback: LocationCallback, myLooper: Looper?) {

}


