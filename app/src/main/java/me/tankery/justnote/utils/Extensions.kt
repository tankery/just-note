package me.tankery.justnote.utils

import android.content.res.AssetManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

fun AssetManager.copyDirOrFile(path: String, dest: File): Int {
    val files = list(path) ?: throw NullPointerException("List $path returns null!")

    return if (files.isEmpty()) {
        // Is file
        copyFile(path, dest)
    } else {
        // Is dir
        files.fold(0) { acc, dir ->
            acc + copyDirOrFile("$path/$dir", File(dest, dir))
        }
    }
}

fun AssetManager.copyFile(path: String, dest: File): Int {
    Timber.d("Copy file from %s to %s", path, dest.absolutePath)

    // Create parent dirs
    dest.parentFile.mkdirs()

    // Copy file content
    open(path).use { inputStream ->
        FileOutputStream(dest).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }

    return 1
}

fun <T, K, S> LiveData<T>.combineWith(
    source: LiveData<K>,
    combine: (data1: T?, data2: K?) -> S) : LiveData<S> =

    object : MediatorLiveData<S>() {

        private var data1: T? = null
        private var data2: K? = null

        init {
            super.addSource(this@combineWith) {
                data1 = it
                value = combine(data1, data2)
            }
            super.addSource(source) {
                data2 = it
                value = combine(data1, data2)
            }
        }

        override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
            throw UnsupportedOperationException()
        }

        override fun <S : Any?> removeSource(toRemote: LiveData<S>) {
            throw UnsupportedOperationException()
        }
    }
