package com.koparde.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isInvisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.koparde.weather.databinding.ActivityMainBinding
import org.json.JSONObject
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")

        getData(lat,long)

        mainBinding.searchIcon.setOnClickListener {

            var cityName:String = mainBinding.editTextCityName.text.toString()
            getLocationData(cityName)
        }

    }

    private fun getLocationData(cityName: String) {

        val apikey = "7eb0b2d87cba2247e32459ad1bd31934"

        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?q=${cityName}&appid=${apikey}"

        val jsonRequest =JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->

                mainBinding.progressBar.isInvisible = true
                mainBinding.relativeLayout.isInvisible = false
                setValues(response)

            },
            { Toast.makeText(this,"Error",Toast.LENGTH_LONG).show() })
        queue.add(jsonRequest)
    }

    private fun getData(lat: String?, long: String?) {
        val apikey = "7eb0b2d87cba2247e32459ad1bd31934"

        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${apikey}"

        val jsonRequest =JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->

                mainBinding.progressBar.isInvisible = true
                mainBinding.relativeLayout.isInvisible = false
                setValues(response)
            },
            { Toast.makeText(this,"Error",Toast.LENGTH_LONG).show() })
        queue.add(jsonRequest)
    }

    private fun setValues(response: JSONObject?) {
        mainBinding.textViewCityName.text = response?.getString("name")

        var temperature = response?.getJSONObject("main")?.getString("temp")
        temperature = ((temperature?.toFloat()!! - 273.15).toInt()).toString()
        mainBinding.textViewTemp.text = temperature+"°C"

        mainBinding.tempCondition.text = response?.getJSONArray("weather")?.getJSONObject(0)?.getString("main")

        var temperatureFeels= response?.getJSONObject("main")?.getString("feels_like")
        temperatureFeels = ((temperatureFeels?.toFloat()!! - 273.15).toInt()).toString()
        mainBinding.tempFeels.text = temperatureFeels+"°C"

        var temperatureMin = response?.getJSONObject("main")?.getString("temp_min")
        temperatureMin = ((temperatureMin?.toFloat()!! - 273.15).toInt()).toString()
        mainBinding.tempMin.text = temperatureMin+"°C"

        var temperatureMax = response?.getJSONObject("main")?.getString("temp_max")
        temperatureMax = (ceil(temperatureMax?.toFloat()!! - 273.15).toInt()).toString()
        mainBinding.tempMax.text = temperatureMax+"°C"

        mainBinding.humidity.text = response?.getJSONObject("main")?.getString("humidity")+"%"
        mainBinding.windSpeed.text = response?.getJSONObject("wind")?.getString("speed")
    }
}