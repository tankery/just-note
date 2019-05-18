package me.tankery.justnote

import android.app.Application
import me.tankery.justnote.utils.Injections

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Injections.application = this
    }
}