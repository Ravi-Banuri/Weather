package com.work.weather.repository

import com.work.weather.network.ApiRequest
import com.work.weather.network.WeatherApi

class WeatherRepository(private val api: WeatherApi) :ApiRequest(){

    suspend fun getWeatherDetails(lat :String, lon: String, units:String, appid: String) = apiRequest { api.fetchWeather(lat, lon, units, appid)}
}