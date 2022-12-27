package com.kdb.jotter.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeNoteDao(private var data: MutableList<Note>) : NoteDao {
    override fun getNotesStream(): Flow<List<Note>> {
        return flowOf(data)
    }

    override suspend fun getNoteById(noteId: Long): Note? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNotesById(ids: List<Long>) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(note: Note): Long {
        TODO("Not yet implemented")
    }

    override suspend fun delete(noteId: NoteId) {
        TODO("Not yet implemented")
    }

    override suspend fun updateContent(noteContent: NoteContent) {
        TODO("Not yet implemented")
    }
}