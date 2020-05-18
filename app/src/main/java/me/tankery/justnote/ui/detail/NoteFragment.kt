package me.tankery.justnote.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import me.tankery.justnote.R
import me.tankery.justnote.data.pojo.NoteDetails
import me.tankery.justnote.vm.NoteDetailsViewModel

/**
 * NoteFragment
 * Created by tankery on 2019-08-01.
 */
class NoteFragment : Fragment() {

    private val args: NoteFragmentArgs by navArgs()

    private val viewModel: NoteDetailsViewModel by viewModels {
        NoteDetailsViewModel.Factory(args.noteId)
    }

    private lateinit var textTitle: TextView
    private lateinit var textContent: TextView
    private lateinit var textHint: TextView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textTitle = view.findViewById(android.R.id.title)
        textContent = view.findViewById(android.R.id.content)
        textHint = view.findViewById(android.R.id.hint)

        viewModel.getDetails()
            .observe(viewLifecycleOwner, {note -> updateDetails(note)})
    }

    private fun updateDetails(note: NoteDetails) {
        textTitle.text = note.note.title
        textContent.text = note.content
        textHint.text = note.tags.joinToString { it.name }
    }
}
