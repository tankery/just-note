package me.tankery.justnote.data.file

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tankery.justnote.utils.Injections
import me.tankery.justnote.utils.NOTE_ENTRANCE_FILE
import me.tankery.justnote.utils.getNotesRoot
import java.io.File

/**
 * NoteFileReader
 * Created by tankery on 2019-08-02.
 */
class NoteFileReader(
    private val noteId: String
) {

    private val notesRoot: File = getNotesRoot(Injections.context)

    /**
     * Read markdown file content, and convert to normal text
     * TODO: 1. Deal with title
     * TODO: 2. Parse markdown file
     */
    fun readContent(): LiveData<String> {
        val liveData = MutableLiveData<String>()
        ArchTaskExecutor.getIOThreadExecutor().execute {
            val entranceFile = File(File(notesRoot, noteId), NOTE_ENTRANCE_FILE)
            val content =
                if (entranceFile.isFile) entranceFile.readText(Charsets.UTF_8)
                else ""
            liveData.postValue(content)
        }
        return liveData
    }
}