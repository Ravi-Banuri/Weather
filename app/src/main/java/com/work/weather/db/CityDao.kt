package com.work.weather.db

import androidx.room.*

@Dao
interface CityDao {
    @Insert
    suspend fun addCity(city: City)

    @Query("Select * from city")
    suspend fun getCities(): List<City>

    @Update
    suspend fun updateCity(city: City)

    @Delete
    suspend fun deleteCity(city: City)
}