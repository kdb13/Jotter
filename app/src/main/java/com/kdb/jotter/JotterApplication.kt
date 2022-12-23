package com.kdb.jotter

import android.app.Application
import com.kdb.jotter.data.AppDatabase
import com.kdb.jotter.data.NotesRepository

class JotterApplication : Application() {
    /*
        Not using any kind of Dependency Injection, right now. Just creating objects and passing them
        inside the constructors.
     */
    val repository by lazy {
        val database = AppDatabase.getInstance(this)
        NotesRepository(database.noteDao())
    }
}