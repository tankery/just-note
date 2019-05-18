package me.tankery.justnote.data.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.toLiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.tankery.justnote.data.db.NoteDatabase
import me.tankery.justnote.data.db.pojo.Tag
import me.tankery.justnote.utils.Injections
import me.tankery.justnote.utils.InjectionsRule
import me.tankery.justnote.utils.date
import me.tankery.justnote.utils.getValueBlocking
import me.tankery.justnote.utils.testTags
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class TagDaoTest {

    @get:Rule
    val injectionsRule = InjectionsRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: NoteDatabase
    private lateinit var tagDao: TagDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(Injections.context, NoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        tagDao = database.tagDao()

        tagDao.insert(testTags)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testGetAllNodes() {
        val tagsFactory = tagDao.getTags()
        val tagList = tagsFactory.toLiveData(10).getValueBlocking()
        val sortedTags = testTags.sortedByDescending { it.createTimestamp }

        assertThat(tagList.size).isEqualTo(sortedTags.size)
        for ((i, expect) in sortedTags.withIndex()) {
            val tagFromDb = tagList[i]
            assertThat(tagFromDb?.id).isEqualTo(expect.id)
            assertThat(tagFromDb?.name).isEqualTo(expect.name)
            assertThat(tagFromDb?.createTimestamp).isEqualTo(expect.createTimestamp)
            assertThat(tagFromDb?.preserved).isEqualTo(expect.preserved)
        }
    }

    @Test
    fun testInsertNew() {
        val newTag = Tag("new", "New", date(Calendar.MAY,12))
        tagDao.insert(newTag)

        val tagsFactory = tagDao.getTags()
        val tagList = tagsFactory.toLiveData(10).getValueBlocking()
        val tagFromDb = tagList.find { it.id == newTag.id }

        assertThat(tagFromDb).isNotNull
        assertThat(tagFromDb?.createTimestamp).isEqualTo(newTag.createTimestamp)
        assertThat(tagFromDb?.name).isEqualTo(newTag.name)
    }

    @Test
    fun testInsertDuplicate() {
        val duplicated = testTags[0].copy(name = "Updated")
        tagDao.insert(duplicated)

        val tagsFactory = tagDao.getTags()
        val tagList = tagsFactory.toLiveData(10).getValueBlocking()
        val tagFromDb = tagList.find { it.id == duplicated.id }

        assertThat(tagFromDb).isNotNull
        assertThat(tagFromDb?.name).isEqualTo(duplicated.name)
    }

    @Test
    fun testInsertListDuplicate() {
        val duplicated1 = testTags[0].copy(name = "Updated")
        val duplicated2 = testTags[1].copy(name = "Updated 2")
        tagDao.insert(listOf(duplicated1, duplicated2))

        val tagsFactory = tagDao.getTags()
        val tagList = tagsFactory.toLiveData(10).getValueBlocking()

        val tagFromDb1 = tagList.find { it.id == duplicated1.id }
        assertThat(tagFromDb1).isNotNull
        assertThat(tagFromDb1?.name).isEqualTo(duplicated1.name)

        val tagFromDb2 = tagList.find { it.id == duplicated2.id }
        assertThat(tagFromDb2).isNotNull
        assertThat(tagFromDb1?.name).isEqualTo(duplicated1.name)
    }

    @Test
    fun testDelete() {
        tagDao.delete("task")

        val tagsFactory = tagDao.getTags()
        val tagList = tagsFactory.toLiveData(10).getValueBlocking()
        val tagFromDb = tagList.find { it.id == "task" }

        assertThat(tagFromDb).isNull()
    }

    @Test
    fun testDeletePreserved() {
        tagDao.delete("_archived_")

        val tagsFactory = tagDao.getTags()
        val tagList = tagsFactory.toLiveData(10).getValueBlocking()
        val tagFromDb = tagList.find { it.id == "_archived_" }

        assertThat(tagFromDb).isNotNull
    }
}