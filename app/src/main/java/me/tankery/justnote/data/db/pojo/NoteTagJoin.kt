package me.tankery.justnote.data.db.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "note_tag_join",
    primaryKeys = ["note_id", "tag_id"],
    foreignKeys = [
        ForeignKey(entity = Note::class,
            parentColumns = ["id"],
            childColumns = ["note_id"]),
        ForeignKey(entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"])
    ],
    indices = [
        Index(value = ["note_id", "tag_id"]),
        Index(value = ["tag_id", "note_id"])
    ]
)
data class NoteTagJoin(
    @ColumnInfo(name = "note_id") val noteId: String,
    @ColumnInfo(name = "tag_id") val tagId: String
)