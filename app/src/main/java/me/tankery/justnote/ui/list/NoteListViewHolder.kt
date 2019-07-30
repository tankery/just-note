package me.tankery.justnote.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.tankery.justnote.R
import me.tankery.justnote.data.pojo.NoteItem

/**
 * NoteListViewHolder
 * Created by tankery on 2019-07-29.
 */
class NoteListViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.list_note_item, parent, false)) {

    private val textTitle: TextView = itemView.findViewById(android.R.id.title)
    private val textSummary: TextView = itemView.findViewById(android.R.id.summary)
    private val textHint: TextView = itemView.findViewById(android.R.id.hint)

    fun bind(note: NoteItem) {
        textTitle.text = note.note.title
        textSummary.text = note.note.abstractContent
        if (note.tags.isEmpty()) {
            textHint.visibility = View.GONE
        } else {
            textHint.visibility = View.VISIBLE
            textHint.text = note.tags.joinToString { it.name }
        }
    }

    fun clear() {
        textTitle.text = null
        textSummary.text = null
        textHint.visibility = View.GONE
    }
}