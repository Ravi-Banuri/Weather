package com.work.weather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.work.weather.db.City
import com.work.weather.db.CityDataBase
import com.work.weather.util.Coroutines
import kotlinx.coroutines.Job
import java.util.*

class CityListViewModel(private val dataBase: CityDataBase) : ViewModel() {

    private lateinit var job: Job

    var deletedItems = MutableLiveData<LinkedList<City>>()

    fun getCityList() :LiveData<List<City>> {
        return dataBase.getCityDao().getCities()
    }

    fun deleteCity(city: City) {
        job = Coroutines.doWorkInIo(
            { dataBase.getCityDao().deleteCity(city) },
            {
                //getCityList()
                var list = LinkedList<City>()
                list.add(city)
                deletedItems.value = list
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}