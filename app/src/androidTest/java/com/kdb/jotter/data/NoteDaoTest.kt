package com.kdb.jotter.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class NoteDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var noteDao: NoteDao

    @Before
    fun setup() {
        database = getDatabase()
        noteDao = database.noteDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    private fun getDatabase(): AppDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Test
    fun getNoteById_noteWithValidId_returnsNote() = runTest {
        noteDao.insert(Note(id = 4, "Test Note", "Test Content"))

        val result = noteDao.getNoteById(4)
        assertThat(result).isNotNull()
        assertThat(result?.id).isEqualTo(4)
    }

    @Test
    fun getNoteById_noteWithInvalidId_returnsNull() = runTest {
        noteDao.insert(Note(id = 4, "Test Note", "Test Content"))

        val result = noteDao.getNoteById(2)
        assertThat(result).isNull()
    }

    @Test
    fun insert_noteWithContent_addsANewNote() = runTest {
        // Create a test note
        val noteWithContent = Note(id = 2, title = null, content = "A sample note")

        // Insert the test note
        noteDao.insert(noteWithContent)

        // Get the result for inspection
        val result = noteDao.getAll().first()

        // Check that the test note is present in the result
        assertThat(result).contains(noteWithContent)
    }

    @Test
    fun insert_noteWithContentAndTitle_addsANewNote() = runTest {
        // Create a test note
        val noteWithContentAndTitle = Note(
            id = 2,
            title = "A sample title",
            content = "A sample note"
        )

        // Insert the test note
        noteDao.insert(noteWithContentAndTitle)

        // Get the result for inspection
        val result = noteDao.getAll().first()

        // Check that the test note is present in the result
        assertThat(result).contains(noteWithContentAndTitle)
    }

    @Test
    fun delete_existingNote_noteIsDeleted() = runTest {
        val testNote = Note(id = 2, "", "")
        noteDao.insert(testNote)
        noteDao.delete(NoteId(2))

        val result = noteDao.getAll().first()
        assertThat(result).doesNotContain(testNote)
    }

    @Test
    fun deleteNotesById_validIds_notesAreDeleted() = runTest {
        val testNotes = listOf(
            Note(id = 2, null, "First note"),
            Note(id = 3, null, "Second note"),
            Note(id = 4, null, "Third note")
        )

        testNotes.forEach { noteDao.insert(it) }

        val noteIds = testNotes.map { it.id }
        noteDao.deleteNotesById(noteIds)

        val result = noteDao.getAll().first()
        testNotes.forEach {
            assertThat(result).doesNotContain(it)
        }
    }

    @Test
    fun updateContent_newTitle_updatesTitle() = runTest {
        val testNote = Note(id = 2, "Old Title", "Old Content")

        noteDao.insert(testNote)

        noteDao.updateContent(NoteContent(id = 2, title = "New Title", testNote.content))

        val result = noteDao.getNoteById(2)
        assertThat(result?.title).isEqualTo("New Title")
    }

    @Test
    fun updateContent_newTitleAndContent_updatesBoth() = runTest {
        val testNote = Note(id = 2, "Old Title", "Old Content")

        noteDao.insert(testNote)

        noteDao.updateContent(NoteContent(id = 2, title = "New Title", "New Content"))

        val result = noteDao.getNoteById(2)
        assertThat(result?.title).isEqualTo("New Title")
        assertThat(result?.content).isEqualTo("New Content")
    }
}