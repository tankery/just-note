package me.tankery.justnote.ui.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import me.tankery.justnote.data.pojo.NoteItem

/**
 * NoteListAdapter
 * Created by tankery on 2019-07-29.
 */
class NoteListAdapter :
    PagedListAdapter<NoteItem, NoteListViewHolder>(NoteItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = NoteListViewHolder(parent)

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val note = getItem(position)
        if (note != null) {
            holder.bind(note)
        } else {
            holder.clear()
        }
    }
}
