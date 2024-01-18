package com.kdb.jotter.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.kdb.jotter.R
import com.kdb.jotter.TAG
import com.kdb.jotter.databinding.FragmentNotesBinding
import com.kdb.jotter.ui.actionmode.PrimaryActionModeCallback
import com.kdb.jotter.ui.adapter.NoteDetailsLookup
import com.kdb.jotter.ui.adapter.NoteKeyProvider
import com.kdb.jotter.ui.adapter.NotesAdapter
import com.kdb.jotter.ui.components.NotesList
import com.kdb.jotter.ui.screens.NotesScreen
import com.kdb.jotter.ui.state.NotesUiState
import com.kdb.jotter.ui.viewmodels.NotesViewModel

class NotesFragment : Fragment() {

    companion object {
        const val SELECTION_ID = "notes_selection"
    }

    private val viewModel: NotesViewModel by viewModels { NotesViewModel.Factory }

    private lateinit var tracker: SelectionTracker<Long>
    private lateinit var adapter: NotesAdapter

    private lateinit var actionMode: PrimaryActionModeCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { 
                NotesScreen(
                    viewModel = viewModel,
                    onNoteClick = { id -> showNoteDetails(id) }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.fragment = this

        // Setup Contextual Action Bar
        actionMode = PrimaryActionModeCallback(R.menu.menu_action_mode) { item ->
            when (item.itemId) {
                R.id.action_delete -> deleteSelectedNotes()
            }
        }

        // setupAdapter()

//        binding.notesComposeView.setContent {
//            val uiState = viewModel.uiState.observeAsState().value!!;
//
//            when (uiState) {
//                NotesUiState.Loading -> Text("Loading notes")
//                is NotesUiState.Success -> NotesList(
//                    notes = uiState.noteItems,
//                    onNoteClick = {id -> showNoteDetails(id) }
//                )
//            }
//        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

//        if (this::tracker.isInitialized) {
//            tracker.onSaveInstanceState(outState)
//        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

//        tracker.onRestoreInstanceState(savedInstanceState)
//        if (tracker.hasSelection()) startActionMode()
    }

    /*private fun setupAdapter() {
        // Create an adapter for displaying notes
        adapter = NotesAdapter { showNoteDetails(it) }
        binding.recyclerViewNotes.adapter = adapter

        // Setup the Selection API with RecyclerView
        setupSelection()

        // Listen for list changes
        subscribeList()
    }*/

    /*private fun setupSelection() {
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
                    startActionMode()
                } else {
                    // Hide the action bar, when there is no selection
                    actionMode.finish()
                }
            }
        })
    }*/

    /**
     * Observe the notes and update the list.
     */
    private fun subscribeList() {
        /*viewModel.uiState.observe(viewLifecycleOwner) { state ->
            Log.d(TAG, "subscribeList: $state")
            
            when (state) {
                is NotesUiState.Loading -> {
                    binding.recyclerViewNotes.isVisible = false
                    binding.emptyStateNotes.isVisible = false
                }

                is NotesUiState.Success -> {
                    adapter.submitList(state.noteItems)

                    binding.recyclerViewNotes.isVisible = !state.isListEmpty
                    binding.emptyStateNotes.isVisible = state.isListEmpty

                    // Notify MainActivity to hide the splash screen
                    requireActivity().supportFragmentManager.setFragmentResult(
                        MainActivity.FRAGMENT_RESULT_REQUEST_KEY,
                        bundleOf(MainActivity.SPLASH_SCREEN_RESULT_KEY to true)
                    )
                }
            }

        }*/
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

    /**
     * Shows the action bar and updates the title.
     */
    /*private fun startActionMode() {
        actionMode.start(binding.recyclerViewNotes)
        actionMode.updateTitle(
            getString(
                R.string.action_mode_selection_title,
                tracker.selection.size()
            )
        )
    }*/
}