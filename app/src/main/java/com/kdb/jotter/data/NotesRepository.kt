package com.kdb.jotter.data

import com.kdb.jotter.ui.state.NoteItemUiState
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotesStream(): Flow<List<NoteItemUiState>>

    suspend fun getNote(id: Long): Note?

    suspend fun addNote(note: Note): Long

    suspend fun deleteNote(noteId: NoteId)

    suspend fun deleteNotes(noteIds: List<Long>)

    suspend fun saveNoteContent(noteContent: NoteContent)
}