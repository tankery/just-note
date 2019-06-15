package me.tankery.justnote

import android.app.Application
import me.tankery.justnote.utils.Injections
import me.tankery.justnote.utils.TAG_MAP
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.uprootAll()
        Timber.plant(TimberTree())

        Injections.application = this
    }
}

class TimberTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return TAG_MAP.find { element.className.startsWith(it.first) }?.second
    }
}