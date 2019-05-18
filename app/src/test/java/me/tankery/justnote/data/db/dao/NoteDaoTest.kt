package me.tankery.justnote.data.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.toLiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.tankery.justnote.data.db.NoteDatabase
import me.tankery.justnote.data.db.pojo.Note
import me.tankery.justnote.utils.Injections
import me.tankery.justnote.utils.InjectionsRule
import me.tankery.justnote.utils.date
import me.tankery.justnote.utils.getValueBlocking
import me.tankery.justnote.utils.testNotes
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    @get:Rule
    val injectionsRule = InjectionsRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: NoteDatabase
    private lateinit var noteDao: NoteDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(Injections.context, NoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        noteDao = database.noteDao()

        noteDao.insert(testNotes)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testGetAllNodes() {
        val notesFactory = noteDao.getNotes()
        val noteList = notesFactory.toLiveData(10).getValueBlocking()
        val sortedNotes = testNotes.sortedByDescending { it.updateTimestamp }

        assertThat(noteList.size).isEqualTo(testNotes.size)
        for ((i, expect) in sortedNotes.withIndex()) {
            val noteFromDb = noteList[i]
            assertThat(noteFromDb?.id).isEqualTo(expect.id)
            assertThat(noteFromDb?.createTimestamp).isEqualTo(expect.createTimestamp)
            assertThat(noteFromDb?.updateTimestamp).isEqualTo(expect.updateTimestamp)
            assertThat(noteFromDb?.title).isEqualTo(expect.title)
            assertThat(noteFromDb?.abstractContent).isEqualTo(expect.abstractContent)
        }
    }

    @Test
    fun testInsertNew() {
        val newNote = Note(createTimestamp = date(Calendar.MAY,12), title = "May day of 12")
        noteDao.insert(newNote)

        val notesFactory = noteDao.getNotes()
        val noteList = notesFactory.toLiveData(10).getValueBlocking()
        val noteFromDb = noteList.find { it.id == newNote.id }

        assertThat(noteFromDb).isNotNull
        assertThat(noteFromDb?.updateTimestamp).isEqualTo(newNote.updateTimestamp)
        assertThat(noteFromDb?.abstractContent).isEqualTo(newNote.abstractContent)
    }

    @Test
    fun testInsertDuplicate() {
        val duplicated = testNotes[0].copy(updateTimestamp = Calendar.getInstance(), abstractContent = "Updated")
        noteDao.insert(duplicated)

        val notesFactory = noteDao.getNotes()
        val noteList = notesFactory.toLiveData(10).getValueBlocking()
        val noteFromDb = noteList.find { it.id == duplicated.id }

        assertThat(noteFromDb).isNotNull
        assertThat(noteFromDb?.updateTimestamp).isEqualTo(duplicated.updateTimestamp)
        assertThat(noteFromDb?.abstractContent).isEqualTo(duplicated.abstractContent)
    }

    @Test
    fun testInsertListDuplicate() {
        val duplicated1 = testNotes[0].copy(updateTimestamp = Calendar.getInstance(), abstractContent = "Updated")
        val duplicated2 = testNotes[2].copy(updateTimestamp = Calendar.getInstance(), abstractContent = "Updated 2")
        noteDao.insert(listOf(duplicated1, duplicated2))

        val notesFactory = noteDao.getNotes()
        val noteList = notesFactory.toLiveData(10).getValueBlocking()

        val noteFromDb1 = noteList.find { it.id == duplicated1.id }
        assertThat(noteFromDb1).isNotNull
        assertThat(noteFromDb1?.updateTimestamp).isEqualTo(duplicated1.updateTimestamp)
        assertThat(noteFromDb1?.abstractContent).isEqualTo(duplicated1.abstractContent)

        val noteFromDb2 = noteList.find { it.id == duplicated2.id }
        assertThat(noteFromDb2).isNotNull
        assertThat(noteFromDb2?.updateTimestamp).isEqualTo(duplicated2.updateTimestamp)
        assertThat(noteFromDb2?.abstractContent).isEqualTo(duplicated2.abstractContent)
    }

    @Test
    fun testDelete() {
        val toBeDelete = testNotes[0].copy()
        noteDao.delete(toBeDelete)

        val notesFactory = noteDao.getNotes()
        val noteList = notesFactory.toLiveData(10).getValueBlocking()
        val noteFromDb = noteList.find { it.id == toBeDelete.id }

        assertThat(noteFromDb).isNull()
    }
}