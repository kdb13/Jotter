package com.kdb.jotter.data

import com.kdb.jotter.ui.state.NoteUiState
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<NoteUiState>> = noteDao.getAll()

    suspend fun getNoteById(noteId: Long): Note = noteDao.getNoteById(noteId)

    suspend fun insert(note: Note): Long = noteDao.insert(note)

    suspend fun delete(note: Note) = noteDao.delete(note)

    suspend fun deleteNotesById(ids: List<Long>) = noteDao.deleteNotesById(ids)

    suspend fun updateContent(noteContent: NoteContent) = noteDao.updateContent(noteContent)
}