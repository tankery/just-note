package me.tankery.justnote.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.tankery.justnote.data.db.pojo.Tag

@Dao
interface TagDao {
    @Query("SELECT * FROM tag ORDER BY create_timestamp DESC")
    fun getTags(): LiveData<List<Tag>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tags: List<Tag>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tag: Tag)

    @Query("DELETE FROM tag WHERE id = :tagId AND preserved != 1")
    fun delete(tagId: String)

}