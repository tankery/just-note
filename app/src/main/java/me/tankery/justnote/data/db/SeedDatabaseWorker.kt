package me.tankery.justnote.data.db

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import me.tankery.justnote.data.db.pojo.Note
import me.tankery.justnote.data.db.pojo.NoteTagJoin
import me.tankery.justnote.data.db.pojo.Tag
import me.tankery.justnote.utils.RES_PRESERVED_TAGS
import me.tankery.justnote.utils.RES_SAMPLE_NOTES
import me.tankery.justnote.utils.RES_TAG_JOINS
import timber.log.Timber
import java.lang.reflect.Type

/**
 * Seeding database with prepared tags
 */
class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result =
        try {
            val tags = readData<List<Tag>>(RES_PRESERVED_TAGS, object : TypeToken<List<Tag>>() {}.type)
            val notes = readData<List<Note>>(RES_SAMPLE_NOTES, object : TypeToken<List<Note>>() {}.type)
            val joins = readData<List<NoteTagJoin>>(RES_TAG_JOINS, object : TypeToken<List<NoteTagJoin>>() {}.type)

            NoteDatabase.instance.tagDao().insert(tags)
            NoteDatabase.instance.noteDao().insert(notes)
            NoteDatabase.instance.noteTagDao().insert(joins)

            Timber.i("Seeding database with %d tags, %d notes and %d joins",
                tags.size, notes.size, joins.size)

            Result.success()
        } catch (ex: Exception) {
            Timber.e(ex, "Error seeding database")
            Result.failure()
        }

    private fun <T> readData(filename : String, type : Type): T =
        applicationContext.assets.open(filename).use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                return Gson().fromJson(jsonReader, type)
            }
        }
}