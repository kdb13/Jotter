package com.kdb.jotter.ui.state

/**
 * UI state for a single note item in the Notes screen.
 *
 * @param id ID of the note. Required for showing note details and keeping track of selections.
 * @param title The note's title. (optional)
 * @param content The note's content.
 */
data class NoteItemUiState(
    val id: Long,
    val title: String?,
    val content: String
) {
    val isTitleVisible: Boolean
        get() = !title.isNullOrEmpty()
}
