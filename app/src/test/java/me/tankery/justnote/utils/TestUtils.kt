package me.tankery.justnote.utils

import me.tankery.justnote.data.db.pojo.Note
import me.tankery.justnote.data.db.pojo.NoteTagJoin
import me.tankery.justnote.data.db.pojo.Tag
import java.util.*

fun date(month: Int, day: Int) : Calendar = Calendar.getInstance().apply {
    set(Calendar.YEAR, 2019)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, day)
}

val testTags = listOf(
    Tag("_deleted_", "Deleted", Calendar.getInstance().apply { timeInMillis = 0 }, preserved = true),
    Tag("_archived_", "Archived", Calendar.getInstance().apply { timeInMillis = 1 }, preserved = true),
    Tag("_pinned_", "Pinned", Calendar.getInstance().apply { timeInMillis = 2 }, preserved = true),
    Tag("task", "Task", date(Calendar.APRIL,18)),
    Tag("good_stuff", "Good Stuff", date(Calendar.JULY,30))
)

val testNotes = listOf(
    Note(id = "note-0", createTimestamp = date(Calendar.FEBRUARY,1), title = "Before the day", abstractContent = "Happy In this day"),
    Note(id = "note-1", createTimestamp = date(Calendar.APRIL,18), title = "April is fool"),
    Note(id = "note-2", createTimestamp = date(Calendar.MAY,3), abstractContent = "May day! May day!"),
    Note(id = "note-3", createTimestamp = date(Calendar.JULY,30), title = "Lovely Summer", abstractContent = "Going to the beach!")
)

val testJoins = listOf(
    NoteTagJoin(testNotes[0].id, "_pinned_"),
    NoteTagJoin(testNotes[0].id, "task"),
    NoteTagJoin(testNotes[1].id, "_archived_"),
    NoteTagJoin(testNotes[2].id, "_archived_"),
    NoteTagJoin(testNotes[2].id, "task")
)