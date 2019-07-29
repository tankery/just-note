package me.tankery.justnote

import android.app.Application
import android.content.pm.PackageManager
import me.tankery.justnote.utils.Injections
import me.tankery.justnote.utils.TAG_MAP
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.uprootAll()
        Timber.plant(TimberTree())

        Injections.application = this

        try {
            val info = packageManager.getPackageInfo(packageName, 0)
            @Suppress("DEPRECATION")
            Timber.i("App created, version %s (%s)", info.versionName, info.versionCode)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.w("App created, can't get version info")
        }
    }
}

class TimberTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return TAG_MAP.find { element.className.startsWith(it.first) }?.second
    }
}