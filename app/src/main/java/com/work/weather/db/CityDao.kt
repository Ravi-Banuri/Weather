package com.work.weather.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {
    @Insert
    suspend fun addCity(city: City)

    @Query("Select * from city")
    fun getCities(): LiveData<List<City>>

    @Update
    suspend fun updateCity(city: City)

    @Delete
    suspend fun deleteCity(city: City)
}