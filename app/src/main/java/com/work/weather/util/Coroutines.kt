package com.work.weather.util

import kotlinx.coroutines.*

object Coroutines {

    /*fun doWorkInIo(task: suspend (() -> Unit) ) =
        CoroutineScope(Dispatchers.Main).launch {
            task()
        }*/
    fun<T: Any> doWorkInIo(task: suspend (() -> T?), callback: ((T?) -> Unit) ) =
        CoroutineScope(Dispatchers.Main).launch {
            val data =CoroutineScope(Dispatchers.IO).async  io@{
                return@io task()
            }.await()
            callback(data)
        }


}
