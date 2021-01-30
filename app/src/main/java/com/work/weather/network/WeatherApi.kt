package com.work.weather.network

import com.work.weather.data.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun fetchWeather(
        @Query("lat") lat: String,
        @Query("lon") lon:String,
        @Query("units") units:String,
        @Query("appid") appid: String) : Response<WeatherResponse>

    companion object {
        operator fun invoke() : WeatherApi{
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .build()
                .create(WeatherApi::class.java)
        }
    }
}