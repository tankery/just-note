package me.tankery.justnote.data

import me.tankery.justnote.data.db.NoteDatabase

class NoteRepository private constructor(database: NoteDatabase) {
    private val noteDao = database.noteDao()
    private val tagDao = database.tagDao()
    private val noteTagDao = database.noteTagDao()

    companion object {
        val instance by lazy { NoteRepository(NoteDatabase.instance) }
    }
}