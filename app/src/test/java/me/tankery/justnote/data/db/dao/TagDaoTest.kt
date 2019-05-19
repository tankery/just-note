package me.tankery.justnote.data.db.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import me.tankery.justnote.data.db.pojo.Tag
import me.tankery.justnote.utils.date
import me.tankery.justnote.utils.findValueBlocking
import me.tankery.justnote.utils.getValueBlocking
import me.tankery.justnote.utils.testTags
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class TagDaoTest : DaoTest() {

    private lateinit var tagDao: TagDao

    @Before
    override fun setUp() {
        super.setUp()
        tagDao = database.tagDao().apply { insert(testTags) }
    }

    @Test
    fun testGetAllNodes() {
        val tagList = tagDao.getTags().getValueBlocking()
        val sortedTags = testTags.sortedByDescending { it.createTimestamp }

        assertThat(tagList.size).isEqualTo(sortedTags.size)
        for ((i, expect) in sortedTags.withIndex()) {
            val actual = tagList[i]
            with(actual) {
                assertThat(id).isEqualTo(expect.id)
                assertThat(name).isEqualTo(expect.name)
                assertThat(createTimestamp).isEqualTo(expect.createTimestamp)
                assertThat(preserved).isEqualTo(expect.preserved)
            }
        }
    }

    @Test
    fun testInsertNew() {
        val newTag = Tag("new", "New", date(Calendar.MAY,12))
        tagDao.insert(newTag)

        val tagFromDb = tagDao.getTags().findValueBlocking { id == newTag.id }

        assertThat(tagFromDb).isNotNull
        assertThat(tagFromDb?.createTimestamp).isEqualTo(newTag.createTimestamp)
        assertThat(tagFromDb?.name).isEqualTo(newTag.name)
    }

    @Test
    fun testInsertDuplicate() {
        val duplicated = testTags[0].copy(name = "Updated")
        tagDao.insert(duplicated)

        val tagFromDb = tagDao.getTags().findValueBlocking { id == duplicated.id }

        assertThat(tagFromDb).isNotNull
        assertThat(tagFromDb?.name).isEqualTo(duplicated.name)
    }

    @Test
    fun testInsertListDuplicate() {
        val duplicated1 = testTags[0].copy(name = "Updated")
        val duplicated2 = testTags[1].copy(name = "Updated 2")
        tagDao.insert(listOf(duplicated1, duplicated2))

        val tagList = tagDao.getTags().getValueBlocking()

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

        val tagFromDb = tagDao.getTags().findValueBlocking { id == "task" }

        assertThat(tagFromDb).isNull()
    }

    @Test
    fun testDeletePreserved() {
        tagDao.delete("_archived_")

        val tagFromDb = tagDao.getTags().findValueBlocking { id == "_archived_" }

        assertThat(tagFromDb).isNotNull
    }
}