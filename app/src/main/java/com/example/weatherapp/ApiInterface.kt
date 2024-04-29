package com.example.weatherapp
import com.example.weatherapp.Model.ModelWet
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather")//1 yaha tha W capital tha// acha
    fun getWeatherData(
        @Query("q") city:String,
        @Query("appid") appid:String,
        @Query("units")units:String

    ) : Call<ModelWet>
}