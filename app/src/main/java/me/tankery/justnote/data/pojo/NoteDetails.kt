package me.tankery.justnote.data.pojo

import me.tankery.justnote.data.db.pojo.Note
import me.tankery.justnote.data.db.pojo.Tag

/**
 * NoteDetails
 * Created by tankery on 2019-08-01.
 */
data class NoteDetails(
    val note: Note,
    val tags: List<Tag> = emptyList(),
    val content: CharSequence = ""
)
