package me.tankery.justnote.utils

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.*

object Injections {
    lateinit var application: Application
    val context: Context
        get() = application

    val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(Calendar::class.java, CalendarJsonDeserializer())
            .create()
    }
}