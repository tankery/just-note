package me.tankery.justnote.ui.list

import androidx.recyclerview.widget.DiffUtil
import me.tankery.justnote.data.pojo.NoteItem

/**
 * NoteItemDiffCallback
 * Created by tankery on 2019-07-29.
 */
class NoteItemDiffCallback : DiffUtil.ItemCallback<NoteItem>() {
    override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem)
            = oldItem.note.id == newItem.note.id

    override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem)
            = oldItem.note == newItem.note
            && oldItem.tags == newItem.tags
}