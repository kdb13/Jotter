package com.kdb.jotter.ui.state

sealed class NotesUiState {

    object Loading : NotesUiState()

    data class Success(val noteItems: List<NoteItemUiState>) : NotesUiState() {
        val isListEmpty = noteItems.isEmpty()
    }

}
