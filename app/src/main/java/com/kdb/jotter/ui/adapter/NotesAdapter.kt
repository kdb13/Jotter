package com.kdb.jotter.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kdb.jotter.databinding.ListItemNoteBinding
import com.kdb.jotter.ui.state.NoteItemUiState

class NotesAdapter(val onNoteClicked: (Long) -> Unit) :
    ListAdapter<NoteItemUiState, NotesAdapter.NoteViewHolder>(DiffCallback()) {

    // The selection tracker used to select notes
    var tracker: SelectionTracker<Long>? = null

    class NoteViewHolder(private val binding: ListItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteItemUiState, isChecked: Boolean) {
            binding.note = note
            binding.cardViewNote.isChecked = isChecked
            binding.textNoteTitle.isVisible = note.isTitleVisible

            // Show full TextView (with no max. lines restriction) for the first note
            if (note.id.toInt() == 1) binding.textNoteContent.maxLines = Int.MAX_VALUE
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long = binding.note!!.id
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ListItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = NoteViewHolder(binding)

        // Call the UI Controller's callback when item is clicked
        binding.root.setOnClickListener {
            onNoteClicked(binding.note!!.id)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        // Bind the note data to ViewHolder
        val note = getItem(position)
        holder.bind(note, tracker!!.isSelected(note.id))
    }

    /**
     * Returns a list of IDs of the selected notes.
     */
    fun getSelectedNotesId(): List<Long> =
        currentList.filter { tracker!!.isSelected(it.id) }.map { it.id }

    private class DiffCallback : DiffUtil.ItemCallback<NoteItemUiState>() {
        override fun areItemsTheSame(oldItem: NoteItemUiState, newItem: NoteItemUiState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NoteItemUiState,
            newItem: NoteItemUiState
        ): Boolean {
            return oldItem.title == newItem.title && oldItem.content == newItem.content
        }
    }
}