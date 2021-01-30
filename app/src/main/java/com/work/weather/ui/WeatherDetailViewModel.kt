package com.work.weather.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.work.weather.R
import com.work.weather.data.Main
import com.work.weather.data.WeatherResponse
import com.work.weather.data.Wind
import com.work.weather.db.City
import com.work.weather.db.CityDataBase
import com.work.weather.repository.WeatherRepository
import com.work.weather.util.Coroutines
import kotlinx.coroutines.Job

class WeatherDetailViewModel(
    val appContext: Application,
    private val repository: WeatherRepository,
    private val city: City
) : AndroidViewModel(appContext) {

    private val appid = "fae7190d7e6433ec3a45285ffcf55c86"

    init {
        getWeather()
    }

    private val _weatherDetails = MutableLiveData<WeatherResponse>()
    val weatherdetail: LiveData<WeatherResponse>
        get() = _weatherDetails

    val _temp = MutableLiveData<Double>()
    val temp: LiveData<Double>
        get() = _temp

    private val _humidity = MutableLiveData<Int>()
    val humidity: LiveData<Int>
        get() = _humidity

    private val _windSpeed = MutableLiveData<Double>()
    val windSpeed: LiveData<Double>
        get() = _windSpeed

    private val _windDeg = MutableLiveData<Int>()
    val windDeg: LiveData<Int>
        get() = _windDeg

    private lateinit var job: Job

    fun getWeather() {
        val units = getSelectedUnits()
        job = Coroutines.doWorkInIo(
            { repository.getWeatherDetails(city.lat, city.longi, units, appid) },
            {
                _weatherDetails.value = it
                weatherdetail.value?.let {
                    _temp.value = weatherdetail.value!!.main.temp
                    _humidity.value = weatherdetail.value!!.main.humidity
                    _windSpeed.value = weatherdetail.value!!.wind.speed
                    _windDeg.value = weatherdetail.value!!.wind.deg
                }

            })
    }

    private fun getSelectedUnits(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(appContext)
        val unitsSelected = prefs.getString(
            appContext.getString(R.string.pref_key_units),
            appContext.getString(R.string.pref_entries_units_standard)
        )
        return unitsSelected!!
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}