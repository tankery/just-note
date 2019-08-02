package me.tankery.justnote.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.DataSource
import me.tankery.justnote.data.db.NoteDatabase
import me.tankery.justnote.data.file.NoteFileReader
import me.tankery.justnote.data.pojo.NoteDetails
import me.tankery.justnote.data.pojo.NoteItem
import me.tankery.justnote.utils.PRESERVED_TAG_ARCHIVED
import me.tankery.justnote.utils.PRESERVED_TAG_DELETED
import me.tankery.justnote.utils.combineWith

class NoteRepository private constructor(database: NoteDatabase) {
    private val noteDao = database.noteDao()
    private val tagDao = database.tagDao()
    private val noteTagDao = database.noteTagDao()

    fun getNotesCount() = noteDao.getCount()

    /**
     * Get user created tags (not preserved tags)
     */
    fun getUserTags() = tagDao.getCustomTags()

    /**
     * Get all notes with specific tag id
     */
    fun getNotesOfTag(tagId: String) = noteTagDao.getNotesOfTag(tagId)

    /**
     * Get all notes with specific tag that not archived or deleted
     */
    fun getActiveNotesOfTag(tagId: String) =
        noteTagDao.getNotesOfTag(tagId, PRESERVED_TAG_DELETED, PRESERVED_TAG_ARCHIVED)

    /**
     * Get all notes that not archived or deleted
     */
    fun getActiveNotes(): DataSource.Factory<Int, NoteItem> =
        noteTagDao.getNotesNotTag(PRESERVED_TAG_DELETED, PRESERVED_TAG_ARCHIVED)
            .map {
                // #map is working on loader's thread, so I think it's safe to
                // execute another query
                // TODO: check the performance of this mapping
                NoteItem(it, noteTagDao.getTagOfNoteDirect(it.id))
            }

    /**
     * Get note details for specific noteId
     */
    fun getNoteDetails(noteId: String): LiveData<NoteDetails> {
        // We first load note metadata, then combine with tags and content
        return noteDao.getNote(noteId)
            .switchMap { note ->
                noteTagDao.getTagOfNote(noteId)
                    .combineWith(NoteFileReader(noteId).readContent()) { tags, content ->
                        return@combineWith NoteDetails(
                            note,
                            tags ?: emptyList(),
                            content ?: "")
                    }
            }
    }

    companion object {
        val instance by lazy { NoteRepository(NoteDatabase.instance) }
    }
}