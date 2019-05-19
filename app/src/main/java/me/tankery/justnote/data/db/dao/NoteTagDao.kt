package me.tankery.justnote.data.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.tankery.justnote.data.db.pojo.Note
import me.tankery.justnote.data.db.pojo.NoteTagJoin
import me.tankery.justnote.data.db.pojo.Tag

@Dao
interface NoteTagDao {
    @Query("""
        SELECT tag.* FROM tag INNER JOIN note_tag_join ON
        tag.id = note_tag_join.tag_id WHERE
        note_tag_join.note_id = :noteId
    """)
    fun getTagOfNote(noteId: String): LiveData<List<Tag>>

    @Query("""
        SELECT note.* FROM note INNER JOIN note_tag_join ON
        note.id=note_tag_join.note_id WHERE
        note_tag_join.tag_id = :tagId
    """)
    fun getNotesOfTag(tagId: String): DataSource.Factory<Int, Note>

    /**
     * The case is a little complicated, that we can not using a INNER JOIN to solve.
     * So I use a sub-query syntax, to first select notes with unwanted tag, then
     * exclude then from the query result.
     */
    @Query("""
        SELECT * FROM note WHERE note.id NOT IN
        (SELECT note_id FROM note_tag_join WHERE
        note_tag_join.tag_id IN (:tagIds))
    """)
    fun getNotesNotTag(vararg tagIds: String): DataSource.Factory<Int, Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg joins: NoteTagJoin)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(joins: List<NoteTagJoin>)

    @Delete
    fun delete(vararg joins: NoteTagJoin)
}