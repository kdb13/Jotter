package com.kdb.jotter.data

import com.kdb.jotter.ui.state.NoteItemUiState
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val noteDao: NoteDao) {

    fun getAllNotes(): Flow<List<NoteItemUiState>> = noteDao.getAll()

    suspend fun getNote(id: Long): Note = noteDao.getNoteById(id)

    suspend fun addNote(note: Note): Long = noteDao.insert(note)

    suspend fun deleteNote(noteId: NoteId) = noteDao.delete(noteId)

    suspend fun deleteNotes(noteIds: List<Long>) = noteDao.deleteNotesById(noteIds)

    suspend fun saveNoteContent(noteContent: NoteContent) = noteDao.updateContent(noteContent)
    
}