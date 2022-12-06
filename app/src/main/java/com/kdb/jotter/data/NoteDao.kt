package com.kdb.jotter.data

import androidx.room.*
import com.kdb.jotter.ui.state.NoteItemUiState
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT id, title, content FROM note ORDER BY dateCreated DESC")
    fun getAll(): Flow<List<NoteItemUiState>>

    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): Note

    @Query("DELETE FROM note WHERE id IN (:ids)")
    suspend fun deleteNotesById(ids: List<Long>)

    @Insert
    suspend fun insert(note: Note): Long

    @Delete(entity = Note::class)
    suspend fun delete(noteId: NoteId)

    @Update(entity = Note::class)
    suspend fun updateContent(noteContent: NoteContent)
}