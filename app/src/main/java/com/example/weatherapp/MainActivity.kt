package com.example.weatherapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import com.example.weatherapp.Model.ModelWet
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private  val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
         fetchWeatherData("noida" )
        searchCity()
    }

    private fun searchCity() {
        val searchView=binding.searchView
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityname:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(cityname, "cc5fd3530a2356bdf42f879a3d6647bf","metric")
        response.enqueue(object : Callback<ModelWet> {

            override fun onResponse(call: Call<ModelWet>, response: Response<ModelWet>)
            {
                val responseBody = response.body()
                Log.d("ResponseApi", response.toString())
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp.toString()
                    val Humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunSet = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val maxtemp = responseBody.main.temp_max
                    val mintemp = responseBody.main.temp_min
                    binding.temp.text = "$temperature °C"
                    binding.weather.text = condition
                    binding.maxTemp.text = "MAX TEMP:$maxtemp °C"
                    binding.minTemp.text = "MIN TEMP:$mintemp °C"
                    binding.Humidity!!.text = "$Humidity%"
                    binding.sunRise!!.text = "${time(sunRise)}"
                    binding.sunset!!.text = "${time(sunSet)}"
                    binding.sea!!.text = "$seaLevel  hPa"
                    binding.windSpeed!!.text = "{$windSpeed} m/s"
                    binding.condition!!.text = condition
                    binding.day.text =dayName(System.currentTimeMillis())
                        binding.date.text =date()
                        binding.cityName.text = "$cityname"

                    // Log.d("TAG","onRresponse:$temperature")
                    changeImagesAccordingtoWeatherConditions(condition)
                }
            }
            // Log.d("TAG","onRresponse:$temperature")

            override fun onFailure(call: Call<ModelWet>, t: Throwable) {
            }
        })
    }

    // Log.d("TAG","onRresponse:$temperature")

    // Log.d("TAG","onRresponse:$temperature")


  /*  private fun fetchWeatherData(cityName:String) {
         ApiClient.apiService.getWeatherData(
            "jaipur","cc5fd3530a2356bdf42f879a3d6647bf","metric")
            .enqueue(object : Callback<ModelWet> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelWet>, response: Response<ModelWet>
                ) {
                    try {
                        val responseBody = response.body()

                        if (response.code() == 404) {
                            Toast.makeText(this@MainActivity,"Something went wrong",Toast.LENGTH_LONG).show()

                        } else if (response.code() == 500) {
                            Toast.makeText(this@MainActivity,"Servier Error",Toast.LENGTH_LONG).show()


                        } else if (response.body()!!.weather.isEmpty()) {
                            Toast.makeText(this@MainActivity,"Data Not found",Toast.LENGTH_LONG).show()

                        } else {
                            val temperature = responseBody!!.main.temp.toString()
                            val Humidity = responseBody.main.humidity
                            val windSpeed = responseBody.wind.speed
                            val sunRise = responseBody.sys.sunrise.toLong()
                            val sunSet = responseBody.sys.sunset.toLong()
                            val seaLevel = responseBody.main.pressure
                            val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                            val maxtemp = responseBody.main.temp_max
                            val mintemp = responseBody.main.temp_min
                            binding.temp.text = "$temperature °F"
                          // binding.temp = response.body()!!.main.temp.toString()+"°F"
                            binding.weather.text = condition
                            binding.maxTemp.text = "MAX TEMP:$maxtemp °F"
                            binding.minTemp.text = "MIN TEMP:$mintemp °F"
                            binding.Humidity!!.text = "$Humidity%"
                            binding.sunRise!!.text = "${time(sunRise)}"
                            binding.sunset!!.text = "${time(sunSet)}"
                            binding.sea!!.text = "$seaLevel  hPa"
                            binding.windSpeed!!.text = "{$windSpeed} m/s"
                            binding.condition!!.text = condition
                            binding.day.text =dayName(System.currentTimeMillis())
                            binding.date.text =date()
                            binding.cityName.text = "$cityName"

                            //now is it working


                             Log.d("TAG","onRresponse:$temperature")
                            changeImagesAccordingtoWeatherConditions(condition)

//Aur kuch? aur???? jo api ka link tha wo sub toh thik kia tha na m n?
                            //sab this tha bas thoda sa issue tha baki sab thik hai th bahut aacha kiya tha acha acha
                            // baki sir logcat m jab m search krti hu toh show ni ho rha tha
                        }
                    } catch (e: Exception) {
                         e.printStackTrace()
                        Toast.makeText(this@MainActivity,"Exception",Toast.LENGTH_LONG).show()

// kuch aise keyword se search kro jo already us me ho acha ek sec
                    }
                }


                override fun onFailure(call: Call<ModelWet>, t: Throwable) {
                    Toast.makeText(this@MainActivity,t.message,Toast.LENGTH_LONG).show()

                }


            })

    }
*/
    private fun changeImagesAccordingtoWeatherConditions(conditions:String) {
        when(conditions){
            "clear sky" ,"Sunny", "Clear"->{
                binding.root.setBackgroundResource(R.drawable.yellow)
               binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

             "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain"->{
             binding.root.setBackgroundResource(R.drawable.rain_background)
                 binding.lottieAnimationView.setAnimation(R.raw.hello)
            }

            "partly cloudy","Clouds","Overcast","Mist","foggy",  "Haze"->{
                binding.root.setBackgroundResource(R.drawable.cloud_back)
              binding.lottieAnimationView.setAnimation(R.raw.bigclouds)
            }

            "Light snow","Moderate","Heavy snow","Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snowyy)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else->{
               binding.root.setBackgroundResource(R.drawable.yellow)
             binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

        }
        binding.lottieAnimationView.pauseAnimation()
    }

    private fun date(): String {
        val sdf= SimpleDateFormat("D MM YYYY", Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun time(timestamp: Long): String {
        val sdf= SimpleDateFormat("HH:MM", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }

    fun dayName(timestamp: Long): String{
            val sdf= SimpleDateFormat("EEEE", Locale.getDefault())
            return sdf.format((Date()))


    }
}