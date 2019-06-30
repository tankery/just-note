package me.tankery.justnote.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import me.tankery.justnote.data.db.dao.NoteDao
import me.tankery.justnote.data.db.dao.NoteTagDao
import me.tankery.justnote.data.db.dao.TagDao
import me.tankery.justnote.data.db.pojo.Note
import me.tankery.justnote.data.db.pojo.NoteTagJoin
import me.tankery.justnote.data.db.pojo.Tag
import me.tankery.justnote.utils.Injections
import me.tankery.justnote.utils.NOTE_DATABASE_NAME
import timber.log.Timber

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
        val instance by lazy { buildDatabase(Injections.context) }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, NoteDatabase::class.java, NOTE_DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Timber.i("Creating database")

                        WorkManager.getInstance().enqueue(OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build())
                        WorkManager.getInstance().enqueue(OneTimeWorkRequestBuilder<SeedFilesystemWorker>().build())
                    }
                })
                .build()
    }
}