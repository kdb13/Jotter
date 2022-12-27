package com.kdb.jotter.data

import com.google.common.truth.Truth.assertThat
import com.kdb.jotter.ui.state.NoteItemUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NotesRepositoryTest {

    private val testNoteItemUiState = NoteItemUiState(
        id = 1,
        title = "Initial Note",
        content = "This is the first note."
    )

    private val testNotes = loadTestNotes()
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setup() {
        val noteDao = FakeNoteDao(testNotes)
        notesRepository = NotesRepository(noteDao)
    }

    @Test
    fun getNotesStream_mapToCorrectUiState() = runTest {
        val result = notesRepository.getNotesStream().first().first()
        assertThat(result).isEqualTo(testNoteItemUiState)
    }

    private fun loadTestNotes() = mutableListOf(
        Note(id = 1, "Initial Note", "This is the first note.")
    )
}