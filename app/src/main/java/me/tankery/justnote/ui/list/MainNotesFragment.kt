package me.tankery.justnote.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.tankery.justnote.R
import me.tankery.justnote.vm.NoteListViewModel

/**
 * MainNotesFragment
 * Created by tankery on 2019-07-30.
 */
class MainNotesFragment : Fragment() {

    private val viewModel: NoteListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.list_note)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val noteListAdapter = NoteListAdapter()
        recyclerView.adapter = noteListAdapter

        viewModel.getNotes()
            .observe(viewLifecycleOwner, {notes -> noteListAdapter.submitList(notes)})
    }

}
