package com.kdb.jotter.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kdb.jotter.ui.state.NoteItemUiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: NoteItemUiState,
    onClick: (id: Long) -> Unit,
    onLongClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    val colors =
        if (selected) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        else CardDefaults.cardColors()

    Card(
        modifier = modifier.combinedClickable(
            onClick = { onClick(note.id) },
            onLongClick = { onLongClick(note.id) }
        ),
        colors = colors
    ) {
        Text(
            text = note.content,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}