package me.tankery.justnote.data.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.toLiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.tankery.justnote.data.db.NoteDatabase
import me.tankery.justnote.data.db.pojo.NoteTagJoin
import me.tankery.justnote.utils.Injections
import me.tankery.justnote.utils.InjectionsRule
import me.tankery.justnote.utils.getValueBlocking
import me.tankery.justnote.utils.testJoins
import me.tankery.justnote.utils.testNotes
import me.tankery.justnote.utils.testTags
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteTagDaoTest {

    @get:Rule
    val injectionsRule = InjectionsRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: NoteDatabase
    private lateinit var noteTagDao: NoteTagDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(Injections.context, NoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        noteTagDao = database.noteTagDao()

        database.noteDao().insert(testNotes)
        database.tagDao().insert(testTags)
        noteTagDao.insert(testJoins)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testGetTagOfNote() {
        val tagsFactory = noteTagDao.getTagOfNote("note-0")
        val tags = tagsFactory.getValueBlocking().map { it.id }

        assertThat(tags).containsExactly("_pinned_", "task")
    }

    @Test
    fun testGetNotesOfTag() {
        val notesFactory = noteTagDao.getNotesOfTag("_archived_")
        val noteIds = notesFactory.toLiveData(10).getValueBlocking().map { it.id }

        assertThat(noteIds).containsExactly("note-1", "note-2")
    }

    @Test
    fun testGetNotesNotTag() {
        val notesFactory = noteTagDao.getNotesNotTag("_deleted_", "_archived_")
        val noteIds = notesFactory.toLiveData(10).getValueBlocking().map { it.id }

        assertThat(noteIds).containsExactly("note-0", "note-3")
    }

    @Test
    fun testInsert() {
        val join = NoteTagJoin(testNotes[3].id, "good_stuff")
        noteTagDao.insert(join)

        val notesFactory = noteTagDao.getNotesOfTag("good_stuff")
        val noteIds = notesFactory.toLiveData(10).getValueBlocking().map { it.id }

        assertThat(noteIds).containsExactly("note-3")
    }

    @Test
    fun testInsertDuplicate() {
        val join = NoteTagJoin(testNotes[0].id, "task")
        noteTagDao.insert(join)

        val notesFactory = noteTagDao.getNotesOfTag("task")
        val noteIds = notesFactory.toLiveData(10).getValueBlocking().map { it.id }

        assertThat(noteIds).contains("note-0")
    }

    @Test
    fun testDelete() {
        val join = NoteTagJoin(testNotes[0].id, "task")
        noteTagDao.delete(join)

        val notesFactory = noteTagDao.getNotesOfTag("task")
        val noteIds = notesFactory.toLiveData(10).getValueBlocking().map { it.id }

        assertThat(noteIds).doesNotContain("note-0")
    }

    @Test
    fun testDeleteNonExist() {
        val join = NoteTagJoin(testNotes[3].id, "task")
        noteTagDao.delete(join)

        val notesFactory = noteTagDao.getNotesOfTag("task")
        val noteIds = notesFactory.toLiveData(10).getValueBlocking().map { it.id }

        assertThat(noteIds).containsExactly("note-0", "note-2")
    }

}