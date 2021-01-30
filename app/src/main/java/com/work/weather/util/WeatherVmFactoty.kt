package com.work.weather.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.work.weather.db.City
import com.work.weather.repository.WeatherRepository
import com.work.weather.ui.WeatherDetailViewModel

class WeatherVmFactoty(private val application: Application, private val repository: WeatherRepository, private val city: City)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherDetailViewModel(application, repository, city) as T
    }
}