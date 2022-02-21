package com.kdb.jotter.data

import com.kdb.jotter.ui.state.NoteItemUiState
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val localDataSource: NotesLocalDataSource) {

    fun getAllNotes(): Flow<List<NoteItemUiState>> = localDataSource.getAllNotes()

    suspend fun getNote(id: Long): Note = localDataSource.getNote(id)

    suspend fun addNote(note: Note): Long = localDataSource.addNote(note)

    suspend fun deleteNote(note: Note) = localDataSource.deleteNote(note)

    suspend fun deleteNotes(noteIds: List<Long>) = localDataSource.deleteNotes(noteIds)

    suspend fun saveNoteContent(noteContent: NoteContent) = localDataSource.saveNoteContent(noteContent)
    
}