package com.kdb.jotter.ui.state

/**
 * The data required by a single note item in the recycler view.
 *
 * @param id the unique ID of note
 * @param content the note's textual content
 */
data class NoteItemUiState(
    val id: Long,
    val content: String
)
