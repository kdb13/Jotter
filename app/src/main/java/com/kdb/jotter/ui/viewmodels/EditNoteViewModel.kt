package com.kdb.jotter.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kdb.jotter.data.Note
import com.kdb.jotter.data.NoteContent
import com.kdb.jotter.data.NoteId
import com.kdb.jotter.data.NotesRepository
import kotlinx.coroutines.launch

class EditNoteViewModel(
    private val repository: NotesRepository,
    private var noteId: Long
) : ViewModel() {

    // The note's title
    val title = MutableLiveData<String>()

    // The note's content
    val content = MutableLiveData<String>()

    val isNewNote: Boolean get() = noteId.compareTo(-1) == 0

    // Whether the user has deleted the current note
    private var isDeleted = false

    init {
        // Load the note's content from repository
        if (!isNewNote) {
            viewModelScope.launch {
                val note = repository.getNote(noteId)
                content.value = note.content
                note.title?.let { title.value = it }
            }
        }
    }

    fun saveNote() {
        // Don't save if already deleted by user
        if (isDeleted) return

        if (isNewNote) addNote() else updateContent()
    }

    fun deleteNote() {
        // Mark as deleted
        isDeleted = true

        // If it is a new note, there is nothing to delete
        if (isNewNote) return

        // Delete the current note
        viewModelScope.launch { repository.deleteNote(NoteId(noteId)) }
    }

    fun isNoteEmpty() = content.value.isNullOrBlank()

    private fun addNote() {
        // Don't add if content is blank
        if (isNoteEmpty()) return

        // Create a new note with content
        val newNote = Note(title = title.value, content = content.value!!)

        // Insert the note
        viewModelScope.launch {
            // Store the ID of newly inserted note
            noteId = repository.addNote(newNote)
        }
    }

    private fun updateContent() {
        // Update the current note with new content
        val newContent = NoteContent(id = noteId, title = title.value, content = content.value!!)
        viewModelScope.launch { repository.saveNoteContent(newContent) }
    }
}

class EditNoteViewModelFactory(private val repository: NotesRepository, private val noteId: Long) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditNoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditNoteViewModel(repository, noteId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}