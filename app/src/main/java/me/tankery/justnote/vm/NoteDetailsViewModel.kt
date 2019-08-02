package me.tankery.justnote.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tankery.justnote.data.NoteRepository
import me.tankery.justnote.data.pojo.NoteDetails

/**
 * NoteDetailsViewModel
 * Created by tankery on 2019-08-01.
 */
class NoteDetailsViewModel(
    private val noteId: String
) : ViewModel() {

    fun getDetails(): LiveData<NoteDetails> =
        NoteRepository.instance.getNoteDetails(noteId)

    class Factory(
        private val noteId: String
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NoteDetailsViewModel(noteId) as T
        }

    }

}
