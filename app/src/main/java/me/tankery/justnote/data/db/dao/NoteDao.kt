package me.tankery.justnote.data.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.tankery.justnote.data.db.pojo.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM note ORDER BY update_timestamp DESC")
    fun getNotes(): DataSource.Factory<Int, Note>

    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNote(noteId: String): LiveData<Note>

    @Query("SELECT COUNT(*) FROM note")
    fun getCount(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notes: List<Note>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Delete
    fun delete(note: Note)

}