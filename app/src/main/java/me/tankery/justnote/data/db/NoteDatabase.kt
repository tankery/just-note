package me.tankery.justnote.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.tankery.justnote.data.db.dao.NoteDao
import me.tankery.justnote.data.db.dao.NoteTagDao
import me.tankery.justnote.data.db.dao.TagDao
import me.tankery.justnote.data.db.pojo.Note
import me.tankery.justnote.data.db.pojo.NoteTagJoin
import me.tankery.justnote.data.db.pojo.Tag
import me.tankery.justnote.utils.NOTE_DATABASE_NAME

@Database(
    entities = [Note::class, Tag::class, NoteTagJoin::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun tagDao(): TagDao
    abstract fun noteTagDao(): NoteTagDao

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): NoteDatabase {
            return Room.databaseBuilder(context, NoteDatabase::class.java,
                NOTE_DATABASE_NAME
            )
                .build()
        }
    }
}