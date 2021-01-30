package com.work.weather.db

import android.content.Context
import androidx.navigation.Navigation
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.work.weather.ui.AddCityFragmentDirections
import com.work.weather.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

@Database(
    entities = [City::class],
    version = 1
)
abstract class CityDataBase : RoomDatabase() {

    abstract fun getCityDao(): CityDao

    companion object {
        private var instance: CityDataBase? = null

        operator fun invoke(context: Context) = instance?: buildCityDataBase(context)

        private fun buildCityDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CityDataBase::class.java,
            "citydatabase").addCallback(object :RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)


            }
        })
            .build()

    }

}