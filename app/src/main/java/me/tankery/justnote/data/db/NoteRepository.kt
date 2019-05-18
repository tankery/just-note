package me.tankery.justnote.data.db

import me.tankery.justnote.utils.Injections

class NoteRepository private constructor(database: NoteDatabase) {
    private val noteDao = database.noteDao()
    private val tagDao = database.tagDao()
    private val noteTagDao = database.noteTagDao()

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: NoteRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: NoteRepository(
                    NoteDatabase.getInstance(Injections.context)
                ).also { instance = it }
            }
    }
}