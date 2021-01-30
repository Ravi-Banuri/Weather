package com.work.weather.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.work.weather.db.CityDataBase
import com.work.weather.ui.CityListViewModel
import com.work.weather.ui.WeatherDetailViewModel

class CityListVmFactory(private val dataBase: CityDataBase)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CityListViewModel(dataBase) as T
    }
}