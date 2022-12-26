package com.kdb.jotter.ui.viewmodels

import androidx.lifecycle.*
import com.kdb.jotter.data.NotesRepository
import com.kdb.jotter.ui.state.NotesUiState
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _uiState = MutableLiveData<NotesUiState>()
    val uiState: LiveData<NotesUiState> = _uiState

    init {
        loadNotes()
    }

    private fun loadNotes() = viewModelScope.launch {
        repository.getAllNotes()
            .onStart { _uiState.value = NotesUiState.Loading }
            .collect {
                _uiState.value = NotesUiState.Success(noteItems = it)
            }
    }

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