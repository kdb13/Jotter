package com.kdb.jotter.ui.state

data class EditNoteUiState(
    val isLoading: Boolean = true,
    val title: String? = null,
    val content: String = ""
) {
    /**
     * Used to hide the options menu while note is being loaded
     */
    val isMenuHidden = isLoading
}