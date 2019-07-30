package me.tankery.justnote.data.pojo

import me.tankery.justnote.data.db.pojo.Note
import me.tankery.justnote.data.db.pojo.Tag

/**
 * NoteItem
 * Created by tankery on 2019-07-30.
 */
data class NoteItem(
    val note: Note,
    val tags: List<Tag> = emptyList()
)