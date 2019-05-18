package me.tankery.justnote.data.db.dao

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import me.tankery.justnote.data.db.NoteDatabase
import me.tankery.justnote.utils.Injections
import me.tankery.justnote.utils.InjectionsRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class DaoTest {
    @get:Rule
    val injectionsRule = InjectionsRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var database: NoteDatabase
        private set

    @CallSuper
    @Before
    open fun setUp() {
        database = Room.inMemoryDatabaseBuilder(Injections.context, NoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    open fun tearDown() {
        database.close()
    }
}