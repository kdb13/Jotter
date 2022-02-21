package com.kdb.jotter.ui.viewmodels

import androidx.lifecycle.*
import com.kdb.jotter.data.NotesRepository
import com.kdb.jotter.ui.state.NoteItemUiState
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    val notes: LiveData<List<NoteItemUiState>> = repository.getAllNotes().asLiveData()

    fun deleteNotes(noteIds: List<Long>) = viewModelScope.launch {
        repository.deleteNotes(noteIds)
    }
}

class NotesViewModelFactory(private val repository: NotesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}