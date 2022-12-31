package com.kdb.jotter.ui.viewmodels

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kdb.jotter.JotterApplication
import com.kdb.jotter.data.NotesRepository
import com.kdb.jotter.ui.state.NotesUiState
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _uiState = MutableLiveData<NotesUiState>(NotesUiState.Loading)
    val uiState: LiveData<NotesUiState> = _uiState

    init {
        loadNotes()
    }

    private fun loadNotes() = viewModelScope.launch {
        repository.getNotesStream()
            .collect {
                _uiState.value = NotesUiState.Success(noteItems = it)
            }
    }

    fun deleteNotes(noteIds: List<Long>) = viewModelScope.launch {
        repository.deleteNotes(noteIds)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as JotterApplication).repository
                NotesViewModel(repository)
            }
        }
    }
}