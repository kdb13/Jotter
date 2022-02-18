package com.kdb.jotter

import android.app.Application
import com.kdb.jotter.data.AppDatabase
import com.kdb.jotter.data.NotesRepository

class JotterApplication : Application() {
    private val database by lazy { AppDatabase.getInstance(this) }

    // Injecting the DAO into repository, for data persistence
    val repository by lazy { NotesRepository(database.noteDao()) }
}