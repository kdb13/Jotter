package com.kdb.jotter.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.kdb.jotter.JotterApplication
import com.kdb.jotter.R
import com.kdb.jotter.TAG
import com.kdb.jotter.databinding.FragmentNotesBinding
import com.kdb.jotter.ui.actionmode.OnActionItemClickListener
import com.kdb.jotter.ui.actionmode.PrimaryActionModeCallback
import com.kdb.jotter.ui.adapter.NoteDetailsLookup
import com.kdb.jotter.ui.adapter.NoteKeyProvider
import com.kdb.jotter.ui.adapter.NotesAdapter
import com.kdb.jotter.ui.state.NotesUiState
import com.kdb.jotter.ui.viewmodels.NotesViewModel
import com.kdb.jotter.ui.viewmodels.NotesViewModelFactory

class NotesFragment : Fragment(), OnActionItemClickListener {

    companion object {
        const val SELECTION_ID = "notes_selection"
    }

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by viewModels {
        NotesViewModelFactory((activity?.application as JotterApplication).repository)
    }

    private lateinit var tracker: SelectionTracker<Long>
    private lateinit var adapter: NotesAdapter

    private var actionMode = PrimaryActionModeCallback(R.menu.menu_action_mode, this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        setupAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        tracker.onRestoreInstanceState(savedInstanceState)

        if (tracker.hasSelection()) {
            actionMode.start(binding.recyclerViewNotes)
            actionMode.updateTitle(getString(R.string.action_mode_selection_title, tracker.selection.size()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Called when an item from the CAB is clicked.
     */
    override fun onActionItemClick(item: MenuItem) {
        when (item.itemId) {
            R.id.action_delete -> deleteSelectedNotes()
        }
    }

    private fun setupAdapter() {
        // Create an adapter for displaying notes
        adapter = NotesAdapter { showNoteDetails(it) }
        binding.recyclerViewNotes.adapter = adapter

        // Setup the Selection API with RecyclerView
        setupSelection()

        // Listen for list changes
        subscribeList()
    }

    private fun setupSelection() {
        // Create a SelectionTracker for selecting notes
        tracker = SelectionTracker.Builder(
            SELECTION_ID,
            binding.recyclerViewNotes,
            NoteKeyProvider(adapter),
            NoteDetailsLookup(binding.recyclerViewNotes),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        adapter.tracker = tracker
        actionMode.tracker = tracker

        // Observe the notes selection
        tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                if (tracker.hasSelection()) {
                    // Show the action bar and update the title
                    actionMode.start(binding.recyclerViewNotes)
                    actionMode.updateTitle(
                        getString(
                            R.string.action_mode_selection_title,
                            tracker.selection.size()
                        )
                    )
                } else {
                    // Hide the action bar, when there is no selection
                    actionMode.finish()
                }
            }
        })
    }

    /**
     * Observe the notes and update the list.
     */
    private fun subscribeList() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->

            when (state) {
                is NotesUiState.Loading -> {
                    binding.recyclerViewNotes.isVisible = false
                    binding.emptyStateNotes.isVisible = false
                }

                is NotesUiState.Success -> {
                    adapter.submitList(state.noteItems)

                    binding.recyclerViewNotes.isVisible = !state.isListEmpty
                    binding.emptyStateNotes.isVisible = state.isListEmpty
                }
            }

        }
    }

    /**
     * Navigates to [EditNoteFragment] for adding a new note.
     */
    fun addNote() {
        val action = NotesFragmentDirections.addNote()
        findNavController().navigate(action)
    }

    /**
     * Navigates to [EditNoteFragment] for viewing/editing a note.
     */
    private fun showNoteDetails(noteID: Long) {
        val action = NotesFragmentDirections.showNoteDetails(noteID)
        findNavController().navigate(action)
    }

    private fun deleteSelectedNotes() {
        viewModel.deleteNotes(adapter.getSelectedNotesId())
    }
}