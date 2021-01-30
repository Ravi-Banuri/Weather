package com.work.weather.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class City (
    val city: String,
    val lat: String,
    val longi: String
) : Serializable{
    @PrimaryKey(autoGenerate = true)
    var  id:Int =0
}