package me.tankery.justnote.data.db.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import me.tankery.justnote.data.db.pojo.Note
import me.tankery.justnote.utils.date
import me.tankery.justnote.utils.findValueBlocking
import me.tankery.justnote.utils.getValueBlocking
import me.tankery.justnote.utils.testNotes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class NoteDaoTest : DaoTest() {

    private lateinit var noteDao: NoteDao

    @Before
    override fun setUp() {
        super.setUp()
        noteDao = database.noteDao().apply { insert(testNotes) }
    }

    @Test
    fun testGetAllNodes() {
        val noteList = noteDao.getNotes().getValueBlocking()
        val sortedNotes = testNotes.sortedByDescending { it.updateTimestamp }

        assertThat(noteList.size).isEqualTo(testNotes.size)
        for ((i, expect) in sortedNotes.withIndex()) {
            val actual = noteList[i]
            with(actual) {
                assertThat(id).isEqualTo(expect.id)
                assertThat(createTimestamp).isEqualTo(expect.createTimestamp)
                assertThat(updateTimestamp).isEqualTo(expect.updateTimestamp)
                assertThat(title).isEqualTo(expect.title)
                assertThat(abstractContent).isEqualTo(expect.abstractContent)
            }
        }
    }

    @Test
    fun testInsertNew() {
        val newNote = Note(createTimestamp = date(Calendar.MAY,12), title = "May day of 12")
        noteDao.insert(newNote)

        val noteFromDb = noteDao.getNotes().findValueBlocking { id == newNote.id }

        assertThat(noteFromDb).isNotNull
        assertThat(noteFromDb?.updateTimestamp).isEqualTo(newNote.updateTimestamp)
        assertThat(noteFromDb?.abstractContent).isEqualTo(newNote.abstractContent)
    }

    @Test
    fun testInsertDuplicate() {
        val duplicated = testNotes[0].copy(updateTimestamp = Calendar.getInstance(), abstractContent = "Updated")
        noteDao.insert(duplicated)

        val noteFromDb = noteDao.getNotes().findValueBlocking { id == duplicated.id }

        assertThat(noteFromDb).isNotNull
        assertThat(noteFromDb?.updateTimestamp).isEqualTo(duplicated.updateTimestamp)
        assertThat(noteFromDb?.abstractContent).isEqualTo(duplicated.abstractContent)
    }

    @Test
    fun testInsertListDuplicate() {
        val duplicated1 = testNotes[0].copy(updateTimestamp = Calendar.getInstance(), abstractContent = "Updated")
        val duplicated2 = testNotes[2].copy(updateTimestamp = Calendar.getInstance(), abstractContent = "Updated 2")
        noteDao.insert(listOf(duplicated1, duplicated2))

        val noteList = noteDao.getNotes().getValueBlocking()

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

        val noteFromDb = noteDao.getNotes().findValueBlocking { id == toBeDelete.id }

        assertThat(noteFromDb).isNull()
    }
}