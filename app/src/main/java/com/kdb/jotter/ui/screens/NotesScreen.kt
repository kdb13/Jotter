package com.kdb.jotter.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.kdb.jotter.ui.components.NotesList
import com.kdb.jotter.ui.state.NotesUiState
import com.kdb.jotter.ui.viewmodels.NotesViewModel

@Composable
fun NotesScreen(
    viewModel: NotesViewModel,
    onNoteClick: (Long) -> Unit
) {
    val uiState = viewModel.uiState.observeAsState().value!!;

    when (uiState) {
        NotesUiState.Loading -> Text("Loading notes")
        is NotesUiState.Success -> NotesList(
            notes = uiState.noteItems,
            onNoteClick = onNoteClick,
            selectionState = viewModel.selectionState
        )
    }
}
