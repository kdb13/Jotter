package com.kdb.jotter.data

import com.kdb.jotter.ui.state.NoteItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultNotesRepository(private val noteDao: NoteDao) : NotesRepository {

    override fun getNotesStream(): Flow<List<NoteItemUiState>> =
        noteDao.getNotesStream().map { notes ->
            notes.map { note ->
                NoteItemUiState(note.id, note.title, note.content)
            }
        }

    override suspend fun getNote(id: Long): Note? = noteDao.getNoteById(id)

    override suspend fun addNote(note: Note): Long = noteDao.insert(note)

    override suspend fun deleteNote(noteId: NoteId) = noteDao.delete(noteId)

    override suspend fun deleteNotes(noteIds: List<Long>) = noteDao.deleteNotesById(noteIds)

    override suspend fun saveNoteContent(noteContent: NoteContent) =
        noteDao.updateContent(noteContent)

}