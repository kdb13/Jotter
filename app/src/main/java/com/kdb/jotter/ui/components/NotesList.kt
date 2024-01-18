package com.kdb.jotter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kdb.jotter.ui.state.NoteItemUiState
import com.kdb.jotter.ui.state.SelectionState

@Composable
fun NotesList(
    notes: List<NoteItemUiState>,
    onNoteClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
    selectionState: SelectionState = remember { SelectionState() },
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            notes,
            key = { note -> note.id }
        ) { note ->
            NoteItem(
                note,
                onClick = {
                    if (!selectionState.handleClick(it)) {
                        onNoteClick(it)
                    }
                },
                onLongClick = selectionState::handleLongClick,
                modifier = Modifier.fillMaxWidth(),
                selected = selectionState.isItemSelected(note.id)
            )
        }
    }
}