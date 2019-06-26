package me.tankery.justnote.data.db

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import me.tankery.justnote.data.db.pojo.Tag
import me.tankery.justnote.utils.RES_PRESERVED_TAGS
import timber.log.Timber

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result =
        try {
            applicationContext.assets.open(RES_PRESERVED_TAGS).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val tagType = object : TypeToken<List<Tag>>() {}.type
                    val tags: List<Tag> = Gson().fromJson(jsonReader, tagType)

                    val database = NoteDatabase.instance
                    database.tagDao().insert(tags)

                    Timber.i("Seeding database with %d tags", tags.size)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex, "Error seeding database")
            Result.failure()
        }
}