package me.tankery.justnote.utils

import android.app.Application
import android.content.Context

object Injections {
    lateinit var application: Application
    val context: Context
        get() = application
}