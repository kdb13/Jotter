package com.kdb.jotter.ui.viewmodels

import androidx.lifecycle.*
import com.kdb.jotter.data.Note
import com.kdb.jotter.data.NoteContent
import com.kdb.jotter.data.NoteId
import com.kdb.jotter.data.NotesRepository
import com.kdb.jotter.ui.state.EditNoteUiState
import kotlinx.coroutines.launch

class EditNoteViewModel(
    private val repository: NotesRepository,
    private var noteId: Long
) : ViewModel() {

    // Whether the user has deleted the current note
    private var isDeleted = false

    // Whether the user is adding a new note or editing an existing one
    val isNewNote: Boolean get() = noteId.compareTo(-1) == 0

    private val _uiState = MutableLiveData<EditNoteUiState>()
    val uiState: LiveData<EditNoteUiState> = _uiState

    init {
        // Load the note details if it's an existing note
        if (!isNewNote) {
            loadNote()
        } else {
            _uiState.value = EditNoteUiState(isLoading = false)
        }
    }

    fun onTitleChanged(title: String) {
        _uiState.value = uiState.value?.copy(title = title)
    }

    fun onContentChanged(content: String) {
        _uiState.value = uiState.value?.copy(content = content)
    }

    fun isNoteEmpty() = _uiState.value?.content.isNullOrBlank()

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

    private fun addNote() {
        // Don't add if content is blank
        if (isNoteEmpty()) return

        // Create a new note with content
        val newNote = Note(
            title = uiState.value!!.title,
            content = uiState.value!!.content
        )

        // Add the note
        viewModelScope.launch {
            repository.addNote(newNote)
        }
    }

    private fun updateContent() {
        // Update the current note with new content
        val newContent = NoteContent(
            id = noteId,
            title = uiState.value!!.title,
            content = uiState.value!!.content
        )

        viewModelScope.launch { repository.saveNoteContent(newContent) }
    }

    private fun loadNote() = viewModelScope.launch {
        // Show the progress bar (isLoading = true)
        _uiState.value = EditNoteUiState()

        // Fetch the note details
        // Will consider for an unknown error in future
        repository.getNote(noteId)?.let { note ->

            // Update UI state with note details & hide the progress bar
            _uiState.value = uiState.value?.copy(
                isLoading = false,
                title = note.title,
                content = note.content
            )

        }
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