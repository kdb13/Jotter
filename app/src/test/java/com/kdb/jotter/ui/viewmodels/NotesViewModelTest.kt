package com.kdb.jotter.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.kdb.jotter.data.FakeNotesRepository
import com.kdb.jotter.data.Note
import com.kdb.jotter.ui.state.NoteItemUiState
import com.kdb.jotter.ui.state.NotesUiState
import com.kdb.jotter.util.MainCoroutineRule
import com.kdb.jotter.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NotesViewModelTest {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notesRepository: FakeNotesRepository

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() = runTest {
        notesRepository = FakeNotesRepository()
        notesViewModel = NotesViewModel(notesRepository)
    }

    @Test
    fun `when view model is created, returns Loading and Success states`() = runTest {
        // Create ViewModel to initiate loading
        notesViewModel = NotesViewModel(notesRepository)

        // Must be Loading
        notesViewModel.uiState.getOrAwaitValue().let { state ->
            assertThat(state is NotesUiState.Loading).isTrue()
        }

        // Finish loading the notes
        advanceUntilIdle()

        // Must be Success
        notesViewModel.uiState.getOrAwaitValue().let { state ->
            assertThat(state is NotesUiState.Success).isTrue()
        }
    }

    @Test
    fun `when notes list is not empty, returns Success state with items`() = runTest {
        // Add some test notes
        val note1 = Note(1, "Title1", "Content1")
        val note2 = Note(2, "Title2", "Content2")
        val note3 = Note(3, "Title3", "Content3")
        notesRepository.addNotes(note1, note2, note3)

        // Run the pending coroutines
        advanceUntilIdle()

        // Must be success
        val uiState = notesViewModel.uiState.getOrAwaitValue()
        assertThat(uiState is NotesUiState.Success).isTrue()

        (uiState as NotesUiState.Success).let { state ->
            // The list must not be empty
            assertThat(state.isListEmpty).isFalse()

            // The notes must match the initial data
            val firstItem = state.noteItems.first()
            val testNoteItemUiState = NoteItemUiState(1, "Title1", "Content1")
            assertThat(firstItem).isEqualTo(testNoteItemUiState)
        }
    }

    @Test
    fun `when notes list is empty, returns Success state with no items`() = runTest {
        // Remove the items
        notesRepository.deleteNotes(listOf(1, 2, 3))

        // Run the pending coroutines
        advanceUntilIdle()

        // Must be success
        val uiState = notesViewModel.uiState.getOrAwaitValue()
        assertThat(uiState is NotesUiState.Success).isTrue()

        (uiState as NotesUiState.Success).let { state ->
            // The list must be empty
            assertThat(state.isListEmpty).isTrue()

            // There must be no items
            assertThat(state.noteItems.isEmpty()).isTrue()
        }
    }
}