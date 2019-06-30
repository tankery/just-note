package me.tankery.justnote.utils

import android.content.res.AssetManager
import timber.log.Timber
import java.io.File

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

//    // Create parent dirs
//    dest.parentFile.mkdirs()
//
//    // Copy file content
//    open(path).use { inputStream ->
//        FileOutputStream(dest).use { outputStream ->
//            inputStream.copyTo(outputStream)
//        }
//    }

    return 1
}