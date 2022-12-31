package com.kdb.jotter.data

import com.kdb.jotter.ui.state.NoteItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeNotesRepository : NotesRepository {

    private val notes = MutableStateFlow(LinkedHashMap<Long, Note>())

    override fun getNotesStream(): Flow<List<NoteItemUiState>> = notes.map {
        it.values.toList().map { note ->
            NoteItemUiState(note.id, note.title, note.content)
        }
    }

    override suspend fun getNote(id: Long): Note? {
        return notes.value[id]
    }

    override suspend fun addNote(note: Note): Long {
        notes.update { notes ->
            val newNotes = LinkedHashMap<Long, Note>(notes)
            newNotes[note.id] = note
            newNotes
        }

        return note.id
    }

    fun addNotes(vararg notesToAdd: Note) {
        notes.update { notes ->
            val newNotes = LinkedHashMap<Long, Note>(notes)
            notesToAdd.forEach { newNotes[it.id] = it }
            newNotes
        }
    }

    override suspend fun deleteNote(noteId: NoteId) {
        notes.update { notes ->
            val newNotes = LinkedHashMap<Long, Note>(notes)
            newNotes.remove(noteId.id)
            newNotes
        }
    }

    override suspend fun deleteNotes(noteIds: List<Long>) {
        notes.update { notes ->
            val newNotes = LinkedHashMap<Long, Note>(notes)
            noteIds.forEach { newNotes.remove(it) }
            newNotes
        }
    }

    override suspend fun saveNoteContent(noteContent: NoteContent) {
        notes.update { notes ->
            val newNotes = LinkedHashMap<Long, Note>(notes)

            val note = Note(
                noteContent.id,
                noteContent.title,
                noteContent.content,
                newNotes[noteContent.id]!!.dateCreated
            )
            newNotes[note.id] = note
            newNotes
        }
    }
}