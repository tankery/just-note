package me.tankery.justnote.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import me.tankery.justnote.data.NoteRepository
import me.tankery.justnote.data.pojo.NoteItem

/**
 * NoteListViewModel
 * Created by tankery on 2019-07-30.
 */
class NoteListViewModel : ViewModel() {

    fun getNotes(): LiveData<PagedList<NoteItem>>
            = NoteRepository.instance.getActiveNotes().toLiveData(pageSize = 20)

}
